package com.example.afinal;

public class Courier {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String vehicleType;
    private String address;
    double distance;

    // Default constructor
    public Courier() {}

    // Constructor with all parameters
    public Courier(String id,String name, String phone, String email, String vehicleType, String address) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.vehicleType = vehicleType;
        this.address = address;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Object getAddress() {
        return address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}