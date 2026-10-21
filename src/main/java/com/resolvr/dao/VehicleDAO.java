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
            (registration_no, model, category, price_per_hour, status)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicle.getRegistrationNo());
            ps.setString(2, vehicle.getModel());
            ps.setString(3, vehicle.getCategory());
            ps.setDouble(4, vehicle.getPricePerHour());
            ps.setString(5, vehicle.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    // Retrieve all vehicles
    public List<Vehicle> getAllVehicles() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT vehicle_id, registration_no, model, category, price_per_hour, status FROM vehicles";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getInt("vehicle_id"),
                    rs.getString("registration_no"),
                    rs.getString("model"),
                    rs.getString("category"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    // Retrieve vehicle by ID
    public Vehicle getVehicleById(int vehicleId) throws SQLException {
        String sql = "SELECT vehicle_id, registration_no, model, category, price_per_hour, status FROM vehicles WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_no"),
                        rs.getString("model"),
                        rs.getString("category"),
                        rs.getDouble("price_per_hour"),
                        rs.getString("status")
                    );
                }
            }
        }

        return null;
    }

    // Retrieve only available vehicles
    public List<Vehicle> getAvailableVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT vehicle_id, registration_no, model, category, price_per_hour, status FROM vehicles WHERE status = 'AVAILABLE'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getInt("vehicle_id"),
                    rs.getString("registration_no"),
                    rs.getString("model"),
                    rs.getString("category"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    // Update a vehicle's hourly rental price
    public boolean updatePrice(int vehicleId, double newPrice)
            throws SQLException {

        String sql =
            "UPDATE vehicles SET price_per_hour = ? WHERE vehicle_id = ?";

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

        String sql = "SELECT vehicle_id, registration_no, model, category, price_per_hour, status FROM vehicles WHERE category = ?";

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
                        rs.getDouble("price_per_hour"),
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
