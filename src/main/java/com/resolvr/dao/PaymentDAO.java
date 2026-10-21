package com.resolvr.dao;

import com.resolvr.model.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // Add a payment to the database
    public boolean addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments " +
                "(rental_id, amount, method, payment_status) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getRentalId());
            statement.setDouble(2, payment.getAmount());
            statement.setString(3, payment.getMethod());
            statement.setString(4, payment.getPaymentStatus());

            return statement.executeUpdate() > 0;
        }
    }

    // Retrieve all payments as Payment objects
    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT payment_id, rental_id, amount, method, " +
                "payment_status, transaction_date FROM payments ORDER BY payment_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("payment_id"),
                        resultSet.getInt("rental_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("method"),
                        resultSet.getString("payment_status"),
                        resultSet.getTimestamp("transaction_date")
                );

                payments.add(payment);
            }
        }

        return payments;
    }

    // Retrieve payments belonging to a particular rental
    public List<Payment> getPaymentsByRentalId(int rentalId)
            throws SQLException {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT payment_id, rental_id, amount, method, " +
                "payment_status, transaction_date FROM payments " +
                "WHERE rental_id = ? ORDER BY payment_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rentalId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = new Payment(
                            resultSet.getInt("payment_id"),
                            resultSet.getInt("rental_id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("method"),
                            resultSet.getString("payment_status"),
                            resultSet.getTimestamp("transaction_date")
                    );

                    payments.add(payment);
                }
            }
        }

        return payments;
    }

    // Retrieve payments for a particular customer
    public List<Payment> getPaymentsByCustomerId(int customerId) throws SQLException {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT p.payment_id, p.rental_id, p.amount, p.method, " +
                     "p.payment_status, p.transaction_date " +
                     "FROM payments p " +
                     "INNER JOIN rentals r ON p.rental_id = r.rental_id " +
                     "WHERE r.customer_id = ? ORDER BY p.payment_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = new Payment(
                            resultSet.getInt("payment_id"),
                            resultSet.getInt("rental_id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("method"),
                            resultSet.getString("payment_status"),
                            resultSet.getTimestamp("transaction_date")
                    );

                    payments.add(payment);
                }
            }
        }

        return payments;
    }

    // Update the status of an existing payment
    public boolean updatePaymentStatus(int paymentId, String status)
            throws SQLException {

        String sql = "UPDATE payments SET payment_status = ? " +
                "WHERE payment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, paymentId);

            return statement.executeUpdate() > 0;
        }
    }
}
