package com.github.mictaege.doozer;

import static com.github.mictaege.doozer.Address.Fields.country;
import static com.github.mictaege.doozer.Address.Fields.street;
import static com.github.mictaege.doozer.Address.Fields.town;
import static com.github.mictaege.doozer.Address.Fields.zip;
import static com.github.mictaege.doozer.Doozer.makeA;
import static com.github.mictaege.doozer.Doozer.makeFrom;
import static com.github.mictaege.doozer.Note.Fields.message;
import static com.github.mictaege.doozer.Person.Fields.address;
import static com.github.mictaege.doozer.Person.Fields.age;
import static com.github.mictaege.doozer.Person.Fields.firstName;
import static com.github.mictaege.doozer.Person.Fields.lastName;
import static com.github.mictaege.doozer.Person.Fields.nickName;
import static com.github.mictaege.doozer.Subject.Fields.id;
import static com.github.mictaege.doozer.Subject.Fields.notes;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import org.junit.Test;


/** */
public class ExamplesSpec {

	@Test
	public void shouldMakeASimplePerson() {
		final Person person = makeA(() -> new Person("Sarah", "Klein"));

		assertThat(person.getFirstName(), is("Sarah"));
		assertThat(person.getLastName(), is("Klein"));
	}

	@Test
	public void shouldMakeAComplexPerson() {
		final Person person = makeA(Person::new,
				p -> p.with(id, "4711"),
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

		assertThat(person.getId(), is("4711"));
		assertThat(person.getFirstName(), is("Sarah"));
		assertThat(person.getLastName(), is("Klein"));
		assertThat(person.getNickName(), is("Sa"));
		assertThat(person.getAge(), is(47));
		assertThat(person.getAddress().getStreet(), is("Mainstreet 8"));
		assertThat(person.getAddress().getZip(), is("49432"));
		assertThat(person.getAddress().getTown(), is("Frankfurt"));
		assertThat(person.getAddress().getCountry(), is("DE"));
		assertThat(person.getNotes().stream().anyMatch(m -> m.getMessage().equals("Call her back!")), is(true));
	}

	@Test
	public void shouldMakePersonsByModifyingATemplate() {
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
		assertThat(mike.getFirstName(), is("Michael"));
		assertThat(mike.getLastName(), is("Kelly"));
		assertThat(mike.getAge(), is(32));

		final Person mikesWife = makeFrom(personTemplate,
				p -> p.but(firstName, "Laura"),
				p -> p.but(age, 29)
		);
		assertThat(mikesWife.getId(), not(mike.getId()));
		assertThat(mikesWife.getFirstName(), is("Laura"));
		assertThat(mikesWife.getLastName(), is("Kelly"));
		assertThat(mikesWife.getAge(), is(29));

		final Person mikesSon = makeFrom(personTemplate,
				p -> p.but(id, "4712"),
				p -> p.but(firstName, "Peter"),
				p -> p.but(age, 5)
		);
		assertThat(mikesSon.getId(), not(mike.getId()));
		assertThat(mikesSon.getFirstName(), is("Peter"));
		assertThat(mikesSon.getLastName(), is("Kelly"));
		assertThat(mikesSon.getAge(), is(5));
	}

	@Test
	public void shouldMakeAPersonUsingThePublicApi() {
		final Person person = makeA(Person::new,
				p -> p.apply(Person::setFirstName, "Sarah"),
				p -> p.apply(Person::setLastName, "Klein"),
				p -> p.apply(Person::setNickName, "Sa"),
				p -> p.apply(Person::setAge, 47),
				p -> p.apply(Person::setAddress, makeA(Address::new,
						a -> a.apply().setStreet("Mainstreet 8"),
						a -> a.apply().setZip("49432"),
						a -> a.apply().setTown("Frankfurt"),
						a -> a.apply().setCountry("DE"))),
				p -> p.apply(Person::addNote, makeA(Note::new,
						n -> n.with(message, "Call her back!")
				))
		);

		assertThat(person.getFirstName(), is("Sarah"));
		assertThat(person.getLastName(), is("Klein"));
		assertThat(person.getNickName(), is("Sa"));
		assertThat(person.getAge(), is(47));
		assertThat(person.getAddress().getStreet(), is("Mainstreet 8"));
		assertThat(person.getAddress().getZip(), is("49432"));
		assertThat(person.getAddress().getTown(), is("Frankfurt"));
		assertThat(person.getAddress().getCountry(), is("DE"));
		assertThat(person.getNotes().stream().anyMatch(m -> m.getMessage().equals("Call her back!")), is(true));

	}

	@Test
	public void shouldMakeAnInvalidStrongPersonality() {
		final StrongPersonality personality = makeA(() -> new StrongPersonality("X", "Y"),
				p -> p.with(StrongPersonality.Fields.id, null),
				p -> p.with(StrongPersonality.Fields.firstName, ""),
				p -> p.with(StrongPersonality.Fields.lastName, null),
				p -> p.with(StrongPersonality.Fields.age, -5)
				);

		assertThat(personality.getId(), is(nullValue()));
		assertThat(personality.getFirstName(), is(""));
		assertThat(personality.getLastName(), is(nullValue()));
		assertThat(personality.getAge(), is(-5));
	}

}