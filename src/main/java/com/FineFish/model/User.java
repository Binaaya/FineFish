package com.FineFish.model;

/**
 * User model class that represents a user in the system.
 * Based on 3NF schema: User (User_id, Username, Fname, Surname, Email, Password, Address, Phone_number, Role)
 */
public class User {
    private int userId;
    private String username;
    private String fname;
    private String surname;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private String role;
    
    // Default constructor
    public User() {
    }
    
    // Constructor with parameters
    public User(int userId, String username, String fname, String surname, String email, 
                String password, String address, String phoneNumber, String role) {
        this.userId = userId;
        this.username = username;
        this.fname = fname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFname() {
        return fname;
    }
    
    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    // For backward compatibility with JSP pages
    public String getName() {
        return fname + " " + surname;
    }
    
    public void setName(String name) {
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            this.fname = parts[0];
            this.surname = parts[1];
        } else {
            this.fname = name;
            this.surname = "";
        }
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}