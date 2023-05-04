package com.example.afinal;

public class User {
    private String address;
    private String email;
    private String fullName;
    private String phoneNo;
    private boolean isAdmin;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String address, String email, String fullName, String phoneNo, boolean isAdmin) {
        this.address = address;
        this.email = email;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.isAdmin = isAdmin;

    }

    public boolean isAdmin() { return isAdmin; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}