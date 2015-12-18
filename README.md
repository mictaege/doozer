![Doozer-Logo](./misc/Doozer_256.png)
# doozer

A minimalistic framework that let you create any Java objects with a fluent and readable syntax. Helpful especially in integration and acceptance test scenarios for building complex object trees.

## First glance

**Create a _Person_ and it's dependencies using _doozer_**
```Java
final Person person = makeA(Person::new,
		p -> p.with(firstName, "Sarah"),
		p -> p.with(lastName, "Klein"),
		p -> p.with(age, 47),
		p -> p.with(address, makeA(Address::new,
				a -> a.with(street, "Mainstreet 8"),
				a -> a.with(zip, "49432"),
				a -> a.with(town, "Frankfurt"),
				a -> a.with(country, "DE"))),
		p -> p.with(notes, asList(makeA(Note::new,
				n -> n.with(message, "Call her back!")
		)))
);
```

## Overview

Creating and dealing with deeply nested object trees could be a mess, especially in testing scenarios. For Unit testing we will use Mocking frameworks to get rid of object dependencies and to take control over the objects behavior. Unfortunately there are testing scenarios like integration and acceptance tests, where we have to deal with real objects trees.

In such situations Test Data Builders are a good approach to make the code more readable and to reduce duplications. The downside of Test Data Builders is that we have to write and maintain additional code.

To make life more easy Nat Pryce invented a tiny framework called [make-it-easy](https://github.com/npryce/make-it-easy). Make-it-easy is a great way to write Test Data Builders with much more less duplications and boilerplate code.

But things are going further and as [Lee Levett noticed in his blog](https://leelevett.wordpress.com/2014/06/27/java-8-lambdas-and-the-builder-pattern/) the new Java 8 language features are offering another approach to write Test Data Builders in a quite elegant way.

Based on the ideas of Nat Pryce and Lee Levett _doozer_ is putting things together and let you write Test Data Builders
- with almost no need for additional boilerplate code
- with the ability to take full control over the objects state - even for immutable objects - without weaken the objects API
- with the possibility to reuse one building strategy as a prototype for a variety of similar objects

## How it works

### Installation

**Maven**
```Xml
<todo/>
```
**Gradle**
```
dependencies {
    testCompile 'todo'
}
```

### Simple example
Given a PoJo _Person_ ...
```Java
public class Person {
	private String firstName;
	private String lastName;
	//getter and setter
}
```
... we have to extend the _Person_ class with meta information about it's instance fields by using an enumeration that implements _DeclaredField_. Only restriction: The names of the enum constants has to be equal - case sensitve - with the instance fields names of the class ...
```Java
public class Person {
	public enum Fields implements DeclaredField<Person> {
		firstName, lastName;
	}
	private String firstName;
	private String lastName;
	//getter and setter
}
```
... That's it!!!

Now we can build _Person_ objects using _doozer_:
```Java
final Person person = makeA(Person::new,
				p -> p.with(firstName, "Sarah"),
				p -> p.with(lastName, "Klein")
				);
```

## Advanced examples

### Building object trees
```Java
final Person person = makeA(Person::new,
				p -> p.with(firstName, "Sarah"),
				p -> p.with(lastName, "Klein"),
				p -> p.with(nickName, "Sa"),
				p -> p.with(age, 47),
				p -> p.with(address, makeA(Address::new,
						a -> a.with(street, "Mainstreet 8"),
						a -> a.with(zip, "49432"),
						a -> a.with(town, "Frankfurt"),
						a -> a.with(country, "DE"))),
				p -> p.with(notes, asList(makeA(Note::new,
						n -> n.with(message, "Call her back!")
					)))
				);
```
**Solution:** Use nested calls of _makeA()_. An indention of the building statements according to the object tree will improve readablity.

### Building objects that has no default constructor
```Java
final Person person = makeA(() -> new Person("Sarah", "Klein"),
				p -> p.with(nickName, "Sa"),
				p -> p.with(age, "47")
				);
```
**Solution:** Provide a lambda instead of a constructor-reference to _makeA()_.

### Modify immutable fields of an object
Given _id_ is an immutable field:
```Java
final Person person = makeA(Person::new,
				p -> p.with(id, "4711"),
				p -> p.with(firstName, "Sarah")
				);
```
**Solution:** There is no difference in dealing with immutable fields.

### Modify inherited fields of an object
Given _id_ is an inherited  field from super class _Subject_:
```Java
final Person person = makeA(Person::new,
				p -> p.with(id, "4711"),//Subject.Fields.id
				p -> p.with(firstName, "Sarah")//Person.Fields.firstName
				);
```
**Solution:** There is no difference in dealing with inherited fields as long as the meta-information is available.

### Use one building strategy for a variety of similar objects
```Java
final Supplier<Person> personTemplate = () -> makeA(Person::new,
				p -> p.with(firstName, "Michael"),
				p -> p.with(lastName, "Kelly"),
				p -> p.with(nickName, "Mike"),
				p -> p.with(age, 32),
				p -> p.with(address, makeA(Address::new,
						a -> a.with(street, "Elmstreet 6"),
						a -> a.with(zip, "23123"),
						a -> a.with(town, "New York"),
						a -> a.with(country, "US")
				)));

final Person mike = makeFrom(personTemplate);

final Person mikesWife = makeFrom(personTemplate,
				p -> p.but(firstName, "Laura"),
				p -> p.but(age, 29)
		);

final Person mikesSon = makeFrom(personTemplate,
				p -> p.but(firstName, "Peter"),
				p -> p.but(age, 5)
		);
```
**Solution:** Define a lambda as a building strategy, use the lambda as a template for building similar objects.

### Using the objects accesable API
Sometime is expedient to call the objects accesable API instead of manipulating the objects instance fields directly. This might be the case if we want to use the functionalty of methods that perform complex calculations.
```Java
final Person person = makeA(Person::new,
				p -> p.apply(Person::setFirstName, "Sarah"),
				p -> p.apply().setLastName("Klein")
		);
```
**Solution:** Use _apply()_ instead of _with()_. _apply()_ comes in two flavours:
- for methods that are taking a single parameter - e.g. setters - you can pass a method-references together with a value to _apply()_
- or you can get access to the builded object itself and then you can call every method of it's accesable API.
