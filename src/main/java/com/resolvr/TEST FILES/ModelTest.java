//this is a test class for nammade payment,return,rental.
package com.resolvr;

import com.resolvr.model.Rental;
import com.resolvr.model.Payment;
import com.resolvr.model.VehicleReturn;

import java.sql.Date;
import java.sql.Timestamp;

public class ModelTest {

    public static void main(String[] args) {

        // Test Rental model
        Rental rental = new Rental(
            1,
            2,
            Date.valueOf("2026-10-20"),
            Date.valueOf("2026-10-23"),
            2400.00
        );

        System.out.println("=== RENTAL ===");
        System.out.println("Customer ID: " + rental.getCustomerId());
        System.out.println("Vehicle ID: " + rental.getVehicleId());
        System.out.println("Total Amount: " + rental.getTotalAmount());
        System.out.println("Status: " + rental.getStatus());

        // Test Payment model
        Payment payment = new Payment(
            1,
            2400.00,
            "UPI",
            "PAID"
        );

        System.out.println("\n=== PAYMENT ===");
        System.out.println("Rental ID: " + payment.getRentalId());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Method: " + payment.getMethod());
        System.out.println("Status: " + payment.getPaymentStatus());

        // Test VehicleReturn model
        VehicleReturn vehicleReturn = new VehicleReturn(
            1,
            Date.valueOf("2026-10-23"),
            200.00,
            0.00,
            "Minor scratches"
        );

        System.out.println("\n=== VEHICLE RETURN ===");
        System.out.println("Rental ID: " + vehicleReturn.getRentalId());
        System.out.println("Return Date: " +
            vehicleReturn.getActualReturnDate());
        System.out.println("Damage Charge: " +
            vehicleReturn.getDamageCharge());
        System.out.println("Refund: " + vehicleReturn.getRefundAmount());
        System.out.println("Condition: " +
            vehicleReturn.getConditionNotes());
    }
}
