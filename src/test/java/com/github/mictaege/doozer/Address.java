package com.github.mictaege.doozer;

/** */
public class Address {

    public enum Fields {
        street, zip, town, country
    }

    private String street;
    private String zip;
    private String town;
    private String country;

    public Address() {
        super();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public String getTown() {
        return town;
    }

    public void setTown(final String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

}
