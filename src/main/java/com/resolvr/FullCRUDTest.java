
package com.resolvr;

import com.resolvr.model.*;
import com.resolvr.service.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FullCRUDTest {

    private static int passed = 0;

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("FAIL: " + message);
        }

        passed++;
        System.out.println("PASS: " + message);
    }

    public static void main(String[] args) {

        VehicleService vehicleService = new VehicleService();
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();
        PaymentService paymentService = new PaymentService();
        ReturnService returnService = new ReturnService();

        // Unique identifiers prevent duplicate registration/licence numbers.
        String runId = String.valueOf(System.currentTimeMillis());
        String registration = "TEST" + runId;
        String license = "TST" + runId;
        String email = "test" + runId + "@example.com";

        int vehicleId;
        int customerId;
        int rentalId;

        try {
            System.out.println("\n===== RESOLVR FULL SERVICE TEST =====");

            // ==========================================
            // 1. VEHICLE SERVICE: CREATE, READ, UPDATE
            // ==========================================
            System.out.println("\n--- VEHICLE SERVICE ---");

            Vehicle vehicle = new Vehicle(
                    registration,
                    "RESOLVR Test Scooter",
                    "Test",
                    123.45
            );

            check(vehicleService.addVehicle(vehicle),
                    "Create vehicle");

            Vehicle savedVehicle = vehicleService.getAllVehicles()
                    .stream()
                    .filter(v -> registration.equals(v.getRegistrationNo()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Created vehicle not found"));

            vehicleId = savedVehicle.getVehicleId();

            check(savedVehicle.getPricePerDay() == 123.45,
                    "Read created vehicle");

            check(vehicleService.searchByCategory("Test")
                            .stream()
                            .anyMatch(v -> v.getVehicleId() == vehicleId),
                    "Search vehicle by category");

            check(vehicleService.updatePrice(vehicleId, 150.00),
                    "Update vehicle price");

            check(vehicleService.getAllVehicles().stream()
                            .anyMatch(v -> v.getVehicleId() == vehicleId
                                    && v.getPricePerDay() == 150.00),
                    "Verify updated vehicle price");

            check(vehicleService.updateStatus(vehicleId, "MAINTENANCE"),
                    "Update vehicle status");

            check(vehicleService.updateStatus(vehicleId, "AVAILABLE"),
                    "Restore vehicle to AVAILABLE");

            // ==========================================
            // 2. CUSTOMER SERVICE: CREATE, READ, UPDATE
            // ==========================================
            System.out.println("\n--- CUSTOMER SERVICE ---");

            Customer customer = new Customer(
                    "RESOLVR Test Customer",
                    "9876543210",
                    email,
                    license
            );

            check(customerService.addCustomer(customer),
                    "Create customer");

            Customer savedCustomer = customerService.getAllCustomers()
                    .stream()
                    .filter(c -> license.equals(c.getLicenseNo()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Created customer not found"));

            customerId = savedCustomer.getCustomerId();

            check(customerService.getCustomerById(customerId) != null,
                    "Read customer by ID");

            savedCustomer.setName("RESOLVR Updated Test Customer");

            check(customerService.updateCustomer(savedCustomer),
                    "Update customer");

            check("RESOLVR Updated Test Customer".equals(
                            customerService.getCustomerById(customerId).getName()),
                    "Verify updated customer");

            // ==========================================
            // 3. RENTAL SERVICE: CREATE BOOKING, READ,
            //    CALCULATE PRICE, UPDATE STATUS
            // ==========================================
            System.out.println("\n--- RENTAL SERVICE ---");

            Date startDate = Date.valueOf(LocalDate.now().plusDays(1));
            Date endDate = Date.valueOf(LocalDate.now().plusDays(3));

            Rental rental = new Rental(
                    customerId,
                    vehicleId,
                    startDate,
                    endDate,
                    0.0
            );

            check(rentalService.addRental(rental),
                    "Create rental booking");

            Rental savedRental = rentalService.getAllRentals()
                    .stream()
                    .filter(r -> r.getVehicleId() == vehicleId
                            && r.getCustomerId() == customerId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Created rental not found"));

            rentalId = savedRental.getRentalId();

            check(savedRental.getTotalAmount() == 300.00,
                    "Verify rental total: 2 days x Rs.150");

            check("BOOKED".equalsIgnoreCase(savedRental.getStatus()),
                    "Verify initial rental status");

            check(rentalService.getRentalById(rentalId) != null,
                    "Read rental by ID");

            check(rentalService.updateRentalStatus(rentalId, "ACTIVE"),
                    "Update rental status to ACTIVE");

            // ==========================================
            // 4. PAYMENT SERVICE: CREATE, READ, UPDATE
            // ==========================================
            System.out.println("\n--- PAYMENT SERVICE ---");

            Payment payment = new Payment(
                    rentalId,
                    300.00,
                    "UPI",
                    "PENDING"
            );

            check(paymentService.addPayment(payment),
                    "Create payment");

            List<Payment> rentalPayments =
                    paymentService.getPaymentsByRentalId(rentalId);

            check(!rentalPayments.isEmpty(),
                    "Read payments by rental ID");

            Payment savedPayment = rentalPayments.get(0);

            check(savedPayment.getAmount() == 300.00,
                    "Verify payment amount");

            check(paymentService.updatePaymentStatus(
                            savedPayment.getPaymentId(), "PAID"),
                    "Update payment status to PAID");

            check(paymentService.getPaymentsByRentalId(rentalId)
                            .stream()
                            .anyMatch(p -> p.getPaymentId()
                                            == savedPayment.getPaymentId()
                                    && "PAID".equalsIgnoreCase(
                                            p.getPaymentStatus())),
                    "Verify updated payment status");

            // ==========================================
            // 5. RETURN SERVICE: CREATE AND READ RETURN
            // ==========================================
            System.out.println("\n--- RETURN SERVICE ---");

            VehicleReturn vehicleReturn = new VehicleReturn(
                    rentalId,
                    Date.valueOf(endDate.toLocalDate().plusDays(1)),
                    0.00,
                    0.00,
                    "RESOLVR automated test return"
            );

            check(returnService.addReturn(vehicleReturn),
                    "Create return record");

            VehicleReturn savedReturn =
                    returnService.getReturnByRentalId(rentalId);

            check(savedReturn != null,
                    "Read return by rental ID");

            check(savedReturn.getDamageCharge() == 0.00
                            && savedReturn.getRefundAmount() == 0.00,
                    "Verify return charges and refund");

            check(rentalService.updateRentalStatus(rentalId, "COMPLETED"),
                    "Mark returned rental as COMPLETED");

            check("COMPLETED".equalsIgnoreCase(
                            rentalService.getRentalById(rentalId).getStatus()),
                    "Verify completed rental status");

            // ==========================================
            // FINAL RESULT
            // ==========================================
            System.out.println("\n==================================");
            System.out.println("ALL TESTS PASSED: " + passed);
            System.out.println("Vehicle ID: " + vehicleId);
            System.out.println("Customer ID: " + customerId);
            System.out.println("Rental ID: " + rentalId);
            System.out.println("Registration: " + registration);
            System.out.println("==================================");

        } catch (Exception e) {
            System.err.println("\nTEST STOPPED: " + e.getMessage());
            e.printStackTrace();
            System.err.println(
                    "\nSome records may already have been created. "
                    + "Check the database before rerunning."
            );
        }
    }
}
