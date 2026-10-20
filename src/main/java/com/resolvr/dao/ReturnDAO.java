
package com.resolvr.dao;

import com.resolvr.model.VehicleReturn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, vehicleReturn.getRentalId());
            statement.setDate(2, vehicleReturn.getActualReturnDate());
            statement.setDouble(3, vehicleReturn.getDamageCharge());
            statement.setDouble(4, vehicleReturn.getRefundAmount());
            statement.setString(5, vehicleReturn.getConditionNotes());

            return statement.executeUpdate() > 0;
        }
    }

    // Retrieve all return records as VehicleReturn objects
    public List<VehicleReturn> getAllReturns() throws SQLException {

        List<VehicleReturn> returns = new ArrayList<>();

        String sql = "SELECT return_id, rental_id, actual_return_date, " +
                "damage_charge, refund_amount, condition_notes " +
                "FROM `returns`";

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
}
