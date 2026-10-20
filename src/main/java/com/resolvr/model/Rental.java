
package com.resolvr.model;

import java.sql.Date;

public class Rental {

    private int rentalId;
    private int customerId;
    private int vehicleId;
    private Date startDate;
    private Date endDate;
    private double totalAmount;
    private String status;

    public Rental(int rentalId, int customerId, int vehicleId,
                  Date startDate, Date endDate,
                  double totalAmount, String status) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Rental(int customerId, int vehicleId,
                  Date startDate, Date endDate,
                  double totalAmount) {
        this(0, customerId, vehicleId, startDate, endDate,
             totalAmount, "ACTIVE");
    }

    public int getRentalId() { return rentalId; }
    public int getCustomerId() { return customerId; }
    public int getVehicleId() { return vehicleId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
