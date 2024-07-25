package com.example.model;
import java.util.UUID;

public class Customer {
    private String uuid;
    private String firstName;
    private String lastName;
    private String street; 
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

    // Constructors
    public Customer(String uuid, String firstName, String lastName, String street, String address, String city, String state, String email, String phone) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }

    public Customer(String firstName, String lastName, String street, String address, String city, String state, String email, String phone) {
    	this.uuid = UUIDUtil.generateTestUUID();
    	this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
