package com.github.mictaege.doozer;

import java.util.UUID;

import org.apache.commons.lang3.Validate;

/** */
public final class StrongPersonality {

	public enum Fields implements DeclaredField<StrongPersonality> {
		id, firstName, lastName, age;
	}

	private final String id;
	private final String firstName;
	private final String lastName;
	private int age;

	public StrongPersonality(final String firstName, final String lastName) {
		super();
		Validate.notEmpty(firstName);
		Validate.notEmpty(lastName);
		this.id = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		Validate.inclusiveBetween(0, 140, age);
		this.age = age;
	}
}