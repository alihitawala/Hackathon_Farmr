package com.climate.farmr.domain;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String state;
    private String Country;

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state == null ? "" : state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return Country == null ? "" : Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
