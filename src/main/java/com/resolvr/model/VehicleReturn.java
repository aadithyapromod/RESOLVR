
package com.resolvr.model;

import java.sql.Date;

public class VehicleReturn {

    private int returnId;
    private int rentalId;
    private Date actualReturnDate;
    private double damageCharge;
    private double refundAmount;
    private String conditionNotes;

    public VehicleReturn(int returnId, int rentalId,
                         Date actualReturnDate,
                         double damageCharge,
                         double refundAmount,
                         String conditionNotes) {
        this.returnId = returnId;
        this.rentalId = rentalId;
        this.actualReturnDate = actualReturnDate;
        this.damageCharge = damageCharge;
        this.refundAmount = refundAmount;
        this.conditionNotes = conditionNotes;
    }

    public VehicleReturn(int rentalId, Date actualReturnDate,
                         double damageCharge,
                         double refundAmount,
                         String conditionNotes) {
        this(0, rentalId, actualReturnDate, damageCharge,
             refundAmount, conditionNotes);
    }

    public int getReturnId() { return returnId; }
    public int getRentalId() { return rentalId; }
    public Date getActualReturnDate() { return actualReturnDate; }
    public double getDamageCharge() { return damageCharge; }
    public double getRefundAmount() { return refundAmount; }
    public String getConditionNotes() { return conditionNotes; }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public void setDamageCharge(double damageCharge) {
        this.damageCharge = damageCharge;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }
}
