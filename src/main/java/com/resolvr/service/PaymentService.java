package com.resolvr.service;

import com.resolvr.dao.PaymentDAO;
import com.resolvr.dao.RentalDAO;
import com.resolvr.model.Payment;
import com.resolvr.model.Rental;

import java.sql.SQLException;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.rentalDAO = new RentalDAO();
    }

    // Record a payment after validating its details
    public boolean addPayment(Payment payment) throws SQLException {

        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment cannot be null."
            );
        }

        if (payment.getRentalId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid rental ID is required."
            );
        }

        if (!Double.isFinite(payment.getAmount())
                || payment.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be positive."
            );
        }

        if (payment.getMethod() == null
                || !(payment.getMethod().equalsIgnoreCase("CASH")
                || payment.getMethod().equalsIgnoreCase("CARD")
                || payment.getMethod().equalsIgnoreCase("UPI"))) {
            throw new IllegalArgumentException(
                    "Payment method must be CASH, CARD or UPI."
            );
        }

        if (payment.getPaymentStatus() == null
                || !(payment.getPaymentStatus().equalsIgnoreCase("PENDING")
                || payment.getPaymentStatus().equalsIgnoreCase("PAID")
                || payment.getPaymentStatus().equalsIgnoreCase("FAILED")
                || payment.getPaymentStatus().equalsIgnoreCase("REFUNDED"))) {
            throw new IllegalArgumentException(
                    "Invalid payment status."
            );
        }

        Rental rental = rentalDAO.getRentalById(payment.getRentalId());

        if (rental == null) {
            throw new IllegalArgumentException(
                    "The specified rental does not exist."
            );
        }

        return paymentDAO.addPayment(payment);
    }

    // Retrieve all payments
    public List<Payment> getAllPayments() throws SQLException {
        return paymentDAO.getAllPayments();
    }

    // Retrieve payments for a particular rental
    public List<Payment> getPaymentsByRentalId(int rentalId)
            throws SQLException {

        if (rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID.");
        }

        return paymentDAO.getPaymentsByRentalId(rentalId);
    }

    // Retrieve payments for a particular customer
    public List<Payment> getPaymentsByCustomerId(int customerId)
            throws SQLException {

        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        return paymentDAO.getPaymentsByCustomerId(customerId);
    }

    // Update payment status
    public boolean updatePaymentStatus(int paymentId, String status)
            throws SQLException {

        if (paymentId <= 0) {
            throw new IllegalArgumentException("Invalid payment ID.");
        }

        if (status == null
                || !(status.equalsIgnoreCase("PENDING")
                || status.equalsIgnoreCase("PAID")
                || status.equalsIgnoreCase("FAILED")
                || status.equalsIgnoreCase("REFUNDED"))) {
            throw new IllegalArgumentException(
                    "Invalid payment status."
            );
        }

        return paymentDAO.updatePaymentStatus(
                paymentId, status.toUpperCase()
        );
    }
}
