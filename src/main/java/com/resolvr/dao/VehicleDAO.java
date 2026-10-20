package com.resolvr.dao;

import com.resolvr.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // Add a vehicle to the database
    public boolean addVehicle(Vehicle vehicle) throws SQLException {

        String sql = """
            INSERT INTO vehicles
            (registration_no, model, category, price_per_day, status)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicle.getRegistrationNo());
            ps.setString(2, vehicle.getModel());
            ps.setString(3, vehicle.getCategory());
            ps.setDouble(4, vehicle.getPricePerDay());
            ps.setString(5, vehicle.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    // Retrieve all vehicles
    public List<Vehicle> getAllVehicles() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getInt("vehicle_id"),
                    rs.getString("registration_no"),
                    rs.getString("model"),
                    rs.getString("category"),
                    rs.getDouble("price_per_day"),
                    rs.getString("status")
                );

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    // Update a vehicle's daily rental price
    public boolean updatePrice(int vehicleId, double newPrice)
            throws SQLException {

        String sql =
            "UPDATE vehicles SET price_per_day = ? WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setInt(2, vehicleId);

            return ps.executeUpdate() > 0;
        }
    }

    // Search vehicles by category
    public List<Vehicle> searchByCategory(String category)
            throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles WHERE category = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_no"),
                        rs.getString("model"),
                        rs.getString("category"),
                        rs.getDouble("price_per_day"),
                        rs.getString("status")
                    ));
                }
            }
        }

        return vehicles;
    }

    // Change vehicle availability
    public boolean updateStatus(int vehicleId, String status)
            throws SQLException {

        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, vehicleId);

            return ps.executeUpdate() > 0;
        }
    }
}
