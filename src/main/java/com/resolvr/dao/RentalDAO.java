package com.resolvr.dao;

import com.resolvr.model.Rental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    // Create a rental
    public boolean addRental(Rental rental) throws SQLException {

        String sql = "INSERT INTO rentals " +
            "(customer_id, vehicle_id, start_date, end_date, " +
            "total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, rental.getCustomerId());
            statement.setInt(2, rental.getVehicleId());
            statement.setDate(3, rental.getStartDate());
            statement.setDate(4, rental.getEndDate());
            statement.setDouble(5, rental.getTotalAmount());
            statement.setString(6, rental.getStatus());

            int affected = statement.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        rental.setRentalId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Retrieve all rentals
    public List<Rental> getAllRentals() throws SQLException {

        List<Rental> rentals = new ArrayList<>();

        String sql = "SELECT * FROM rentals ORDER BY rental_id DESC";

        try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                    connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Rental rental = new Rental(
                    resultSet.getInt("rental_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getInt("vehicle_id"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getDouble("total_amount"),
                    resultSet.getString("status")
                );

                rentals.add(rental);
            }
        }

        return rentals;
    }

    // Find a rental by ID
    public Rental getRentalById(int rentalId) throws SQLException {

        String sql = "SELECT * FROM rentals WHERE rental_id = ?";

        try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                    connection.prepareStatement(sql)) {

            statement.setInt(1, rentalId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Rental(
                        resultSet.getInt("rental_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("status")
                    );
                }
            }
        }

        return null;
    }

    // Retrieve rentals for a specific customer
    public List<Rental> getRentalsByCustomerId(int customerId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();

        String sql = "SELECT * FROM rentals WHERE customer_id = ? ORDER BY rental_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Rental rental = new Rental(
                        resultSet.getInt("rental_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("status")
                    );

                    rentals.add(rental);
                }
            }
        }

        return rentals;
    }

    // Update rental status
    public boolean updateRentalStatus(
            int rentalId, String status) throws SQLException {

        String sql =
            "UPDATE rentals SET status = ? WHERE rental_id = ?";

        try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                    connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, rentalId);

            return statement.executeUpdate() > 0;
        }
    }
}
