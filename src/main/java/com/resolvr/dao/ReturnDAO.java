package com.resolvr.dao;

import com.resolvr.model.VehicleReturn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {

    // Add a vehicle return record
    public boolean addReturn(VehicleReturn vehicleReturn)
            throws SQLException {

        String sql = "INSERT INTO `returns` " +
                "(rental_id, actual_return_date, damage_charge, " +
                "refund_amount, condition_notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, vehicleReturn.getRentalId());
            statement.setDate(2, vehicleReturn.getActualReturnDate());
            statement.setDouble(3, vehicleReturn.getDamageCharge());
            statement.setDouble(4, vehicleReturn.getRefundAmount());
            statement.setString(5, vehicleReturn.getConditionNotes());

            int affected = statement.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        vehicleReturn.setReturnId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Retrieve all return records as VehicleReturn objects
    public List<VehicleReturn> getAllReturns() throws SQLException {

        List<VehicleReturn> returns = new ArrayList<>();

        String sql = "SELECT return_id, rental_id, actual_return_date, " +
                "damage_charge, refund_amount, condition_notes " +
                "FROM `returns` ORDER BY return_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                VehicleReturn vehicleReturn = new VehicleReturn(
                        resultSet.getInt("return_id"),
                        resultSet.getInt("rental_id"),
                        resultSet.getDate("actual_return_date"),
                        resultSet.getDouble("damage_charge"),
                        resultSet.getDouble("refund_amount"),
                        resultSet.getString("condition_notes")
                );

                returns.add(vehicleReturn);
            }
        }

        return returns;
    }

    // Retrieve a return record using its rental ID
    public VehicleReturn getReturnByRentalId(int rentalId)
            throws SQLException {

        String sql = "SELECT return_id, rental_id, actual_return_date, " +
                "damage_charge, refund_amount, condition_notes " +
                "FROM `returns` WHERE rental_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rentalId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new VehicleReturn(
                            resultSet.getInt("return_id"),
                            resultSet.getInt("rental_id"),
                            resultSet.getDate("actual_return_date"),
                            resultSet.getDouble("damage_charge"),
                            resultSet.getDouble("refund_amount"),
                            resultSet.getString("condition_notes")
                    );
                }
            }
        }

        return null;
    }

    // Retrieve return records for a specific customer
    public List<VehicleReturn> getReturnsByCustomerId(int customerId)
            throws SQLException {

        List<VehicleReturn> returns = new ArrayList<>();

        String sql = "SELECT ret.return_id, ret.rental_id, ret.actual_return_date, " +
                "ret.damage_charge, ret.refund_amount, ret.condition_notes " +
                "FROM `returns` ret " +
                "INNER JOIN rentals r ON ret.rental_id = r.rental_id " +
                "WHERE r.customer_id = ? " +
                "ORDER BY ret.return_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    returns.add(new VehicleReturn(
                            resultSet.getInt("return_id"),
                            resultSet.getInt("rental_id"),
                            resultSet.getDate("actual_return_date"),
                            resultSet.getDouble("damage_charge"),
                            resultSet.getDouble("refund_amount"),
                            resultSet.getString("condition_notes")
                    ));
                }
            }
        }

        return returns;
    }

    // Retrieve a return record by return ID
    public VehicleReturn getReturnById(int returnId) throws SQLException {

        String sql = "SELECT return_id, rental_id, actual_return_date, " +
                "damage_charge, refund_amount, condition_notes " +
                "FROM `returns` WHERE return_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, returnId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new VehicleReturn(
                            resultSet.getInt("return_id"),
                            resultSet.getInt("rental_id"),
                            resultSet.getDate("actual_return_date"),
                            resultSet.getDouble("damage_charge"),
                            resultSet.getDouble("refund_amount"),
                            resultSet.getString("condition_notes")
                    );
                }
            }
        }

        return null;
    }
}
