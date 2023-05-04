package com.example.afinal;

import java.util.List;

public class Order {
    private String orderId;
    private String collectionDate;
    private String deliveryDate;
    private double price;
    private String userAddress;
    private Laundromat selectedLaundromat;
    private Courier assignedCourier;
    private String courierEmail;
    private Status status;


    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String collectionDate, String deliveryDate, double price, String userAddress, Laundromat selectedLaundromat) {
        this.collectionDate = collectionDate;
        this.deliveryDate = deliveryDate;
        this.price = price;
        this.userAddress = userAddress;
        this.status = Status.PENDING;
    }


    // getter for courier email
    public String getCourierEmail() {
        return courierEmail;
    }


    // getter for courier email
    public void setCourierEmail(String courierEmail) {
        this.courierEmail = courierEmail;
    }

    public Courier getAssignedCourier() {
        return assignedCourier;
    }

    public void setAssignedCourier(Courier assignedCourier) {
        this.assignedCourier = assignedCourier;
    }

    public void matchCourier(List<Courier> couriers) {
        // Add your matching logic here to select the best courier from the list
        // and assign it to the order
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Laundromat getSelectedLaundromat(){return selectedLaundromat;}

    public void setSelectedLaundromat(Laundromat selectedLaundromat){this.selectedLaundromat = selectedLaundromat;}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
