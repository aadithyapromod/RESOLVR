
package com.resolvr;

import com.resolvr.dao.VehicleDAO;
import com.resolvr.dao.CustomerDAO;
import com.resolvr.dao.RentalDAO;
import com.resolvr.dao.PaymentDAO;
import com.resolvr.dao.ReturnDAO;

import com.resolvr.model.Vehicle;
import com.resolvr.model.Customer;
import com.resolvr.model.Rental;
import com.resolvr.model.Payment;
import com.resolvr.model.VehicleReturn;

import java.sql.SQLException;
import java.util.List;

public class DAOTest {

    public static void main(String[] args) {

        VehicleDAO vehicleDAO = new VehicleDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        RentalDAO rentalDAO = new RentalDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ReturnDAO returnDAO = new ReturnDAO();

        try {

            // Test VehicleDAO
            System.out.println("\n========== VEHICLES ==========");

            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();

            for (Vehicle vehicle : vehicles) {
                System.out.println(
                    "Vehicle ID: " + vehicle.getVehicleId()
                    + " | Registration: " + vehicle.getRegistrationNo()
                    + " | Model: " + vehicle.getModel()
                    + " | Category: " + vehicle.getCategory()
                    + " | Price/Day: " + vehicle.getPricePerDay()
                    + " | Status: " + vehicle.getStatus()
                );
            }

            System.out.println("Total vehicles: " + vehicles.size());


            // Test CustomerDAO
            System.out.println("\n========== CUSTOMERS ==========");

            List<Customer> customers = customerDAO.getAllCustomers();

            for (Customer customer : customers) {
                System.out.println(
                    "Customer ID: " + customer.getCustomerId()
                    + " | Name: " + customer.getName()
                    + " | Phone: " + customer.getPhone()
                    + " | Email: " + customer.getEmail()
                    + " | License: " + customer.getLicenseNo()
                );
            }

            System.out.println("Total customers: " + customers.size());


            // Test RentalDAO
            System.out.println("\n========== RENTALS ==========");

            List<Rental> rentals = rentalDAO.getAllRentals();

            for (Rental rental : rentals) {
                System.out.println(
                    "Rental ID: " + rental.getRentalId()
                    + " | Customer ID: " + rental.getCustomerId()
                    + " | Vehicle ID: " + rental.getVehicleId()
                    + " | Start: " + rental.getStartDate()
                    + " | End: " + rental.getEndDate()
                    + " | Amount: " + rental.getTotalAmount()
                    + " | Status: " + rental.getStatus()
                );
            }

            System.out.println("Total rentals: " + rentals.size());


            // Test PaymentDAO
            System.out.println("\n========== PAYMENTS ==========");

            List<Payment> payments = paymentDAO.getAllPayments();

            for (Payment payment : payments) {
                System.out.println(
                    "Payment ID: " + payment.getPaymentId()
                    + " | Rental ID: " + payment.getRentalId()
                    + " | Amount: " + payment.getAmount()
                    + " | Method: " + payment.getMethod()
                    + " | Status: " + payment.getPaymentStatus()
                    + " | Date: " + payment.getTransactionDate()
                );
            }

            System.out.println("Total payments: " + payments.size());


            // Test ReturnDAO
            System.out.println("\n========== RETURNS ==========");

            List<VehicleReturn> returns = returnDAO.getAllReturns();

            for (VehicleReturn vehicleReturn : returns) {
                System.out.println(
                    "Return ID: " + vehicleReturn.getReturnId()
                    + " | Rental ID: " + vehicleReturn.getRentalId()
                    + " | Return Date: " + vehicleReturn.getActualReturnDate()
                    + " | Damage Charge: " + vehicleReturn.getDamageCharge()
                    + " | Refund: " + vehicleReturn.getRefundAmount()
                    + " | Condition: " + vehicleReturn.getConditionNotes()
                );
            }

            System.out.println("Total returns: " + returns.size());

            System.out.println("\n========== ALL DAO READ TESTS COMPLETED ==========");

        } catch (SQLException e) {
            System.out.println("\nDAO TEST FAILED!");
            e.printStackTrace();
        }
    }
}
