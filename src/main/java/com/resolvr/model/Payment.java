
package com.resolvr.model;

import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int rentalId;
    private double amount;
    private String method;
    private String paymentStatus;
    private Timestamp transactionDate;

    public Payment(int paymentId, int rentalId, double amount,
                   String method, String paymentStatus,
                   Timestamp transactionDate) {
        this.paymentId = paymentId;
        this.rentalId = rentalId;
        this.amount = amount;
        this.method = method;
        this.paymentStatus = paymentStatus;
        this.transactionDate = transactionDate;
    }

    public Payment(int rentalId, double amount,
                   String method, String paymentStatus) {
        this(0, rentalId, amount, method, paymentStatus, null);
    }

    public int getPaymentId() { return paymentId; }
    public int getRentalId() { return rentalId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getPaymentStatus() { return paymentStatus; }
    public Timestamp getTransactionDate() { return transactionDate; }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
