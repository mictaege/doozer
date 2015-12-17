# doozer
![Doozer](./misc/Doozer_256.png)

A minimalistic framework that let you create any Java objects with a fluent and readable syntax.

##Example

### Create a Person with plain Java

```Java
final Person person = new Person();
person.setFirstName("Sarah");
person.setLastName("Klein");
person.setAge(47);
final Address address = new Address();
address.setStreet("Mainstreet 8");
address.setZip("49432");
address.setTown("Frankfurt");
address.setCountry("DE");
person.setAddress(address);
final Note note = new Note();
note.setMessage("Call her back!");
person.addNote(note);
```
### Create a Person with using doozer

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
