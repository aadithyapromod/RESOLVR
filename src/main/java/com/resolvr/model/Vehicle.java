package com.resolvr.model;
public class Vehicle {

    private int vehicleId;
    private String registrationNo;
    private String model;
    private String category;
    private double pricePerDay;
    private String status;

    public Vehicle(int vehicleId, String registrationNo,
                   String model, String category,
                   double pricePerDay, String status) {
        this.vehicleId = vehicleId;
        this.registrationNo = registrationNo;
        this.model = model;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public Vehicle(String registrationNo, String model,
                   String category, double pricePerDay) {
        this(0, registrationNo, model, category,
             pricePerDay, "AVAILABLE");
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

    public double getPricePerDay() {
        return pricePerDay;
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

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
