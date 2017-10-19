package com.github.mictaege.doozer;

public class FaultyPerson {

    public enum Fields {
        firstName, lastName, nickName
    }

    private String firstName;
    private String lastName;

    public FaultyPerson() throws Exception {
        super();
        throw new Exception("Names has to be provided");
    }

    public FaultyPerson(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() throws Exception {
        throw new Exception("Not implemented yet");
    }

    public void setNickName(final String nickName) throws Exception {
        throw new Exception("Not implemented yet");
    }

}
