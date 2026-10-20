
package com.resolvr;

import com.resolvr.model.*;
import com.resolvr.service.*;

public class ServiceTest {

    public static void main(String[] args) {

        VehicleService vehicleService = new VehicleService();
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();
        PaymentService paymentService = new PaymentService();
        ReturnService returnService = new ReturnService();

        System.out.println("==================================");
        System.out.println("       RESOLVR SERVICE TEST");
        System.out.println("==================================");

        // 1. VEHICLE SERVICE
        System.out.println("\n[1] VEHICLE SERVICE");

        try {
            System.out.println(
                "Vehicles found: " +
                vehicleService.getAllVehicles().size()
            );

            try {
                vehicleService.addVehicle(
                    new Vehicle("", "", "", -100)
                );
                System.out.println("FAIL: Invalid vehicle accepted.");
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: Invalid vehicle rejected.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        // 2. CUSTOMER SERVICE
        System.out.println("\n[2] CUSTOMER SERVICE");

        try {
            System.out.println(
                "Customers found: " +
                customerService.getAllCustomers().size()
            );

            try {
                customerService.addCustomer(
                    new Customer("", "", "", "")
                );
                System.out.println("FAIL: Invalid customer accepted.");
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: Invalid customer rejected.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        // 3. RENTAL SERVICE
        System.out.println("\n[3] RENTAL SERVICE");

        try {
            System.out.println(
                "Rentals found: " +
                rentalService.getAllRentals().size()
            );

            try {
                rentalService.getRentalById(-1);
                System.out.println("FAIL: Invalid rental ID accepted.");
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: Invalid rental ID rejected.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        // 4. PAYMENT SERVICE
        System.out.println("\n[4] PAYMENT SERVICE");

        try {
            System.out.println(
                "Payments found: " +
                paymentService.getAllPayments().size()
            );

            try {
                paymentService.addPayment(
                    new Payment(-1, -100, "INVALID", "INVALID")
                );
                System.out.println("FAIL: Invalid payment accepted.");
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: Invalid payment rejected.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        // 5. RETURN SERVICE
        System.out.println("\n[5] RETURN SERVICE");

        try {
            System.out.println(
                "Return records found: " +
                returnService.getAllReturns().size()
            );

            try {
                returnService.getReturnByRentalId(-1);
                System.out.println("FAIL: Invalid rental ID accepted.");
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: Invalid rental ID rejected.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        System.out.println("\n==================================");
        System.out.println("       SERVICE TEST FINISHED");
        System.out.println("==================================");
    }
}
