package com.resolvr.model;

public class Vehicle {

    private int vehicleId;
    private String registrationNo;
    private String model;
    private String category;
    private double pricePerHour;
    private String status;

    public Vehicle(int vehicleId, String registrationNo,
                   String model, String category,
                   double pricePerHour, String status) {
        this.vehicleId = vehicleId;
        this.registrationNo = registrationNo;
        this.model = model;
        this.category = category;
        this.pricePerHour = pricePerHour;
        this.status = status;
    }

    public Vehicle(String registrationNo, String model,
                   String category, double pricePerHour) {
        this(0, registrationNo, model, category,
             pricePerHour, "AVAILABLE");
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public double getPricePerDay() {
        return pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerHour = pricePerDay;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
