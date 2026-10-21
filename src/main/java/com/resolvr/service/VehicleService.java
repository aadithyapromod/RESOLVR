package com.resolvr.service;

import com.resolvr.dao.VehicleDAO;
import com.resolvr.model.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO;

    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }

    public boolean addVehicle(Vehicle vehicle) throws SQLException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        if (isBlank(vehicle.getRegistrationNo())
                || isBlank(vehicle.getModel())
                || isBlank(vehicle.getCategory())) {
            throw new IllegalArgumentException(
                    "Registration number, model and category are required."
            );
        }

        if (!Double.isFinite(vehicle.getPricePerHour())
                || vehicle.getPricePerHour() <= 0) {
            throw new IllegalArgumentException(
                    "Price per hour must be a positive amount."
            );
        }

        return vehicleDAO.addVehicle(vehicle);
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        return vehicleDAO.getAllVehicles();
    }

    public Vehicle getVehicleById(int vehicleId) throws SQLException {
        if (vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle ID.");
        }
        return vehicleDAO.getVehicleById(vehicleId);
    }

    public List<Vehicle> getAvailableVehicles() throws SQLException {
        return vehicleDAO.getAvailableVehicles();
    }

    public List<Vehicle> searchByCategory(String category)
            throws SQLException {
        if (isBlank(category)) {
            throw new IllegalArgumentException(
                    "Category cannot be empty."
            );
        }

        return vehicleDAO.searchByCategory(category.trim());
    }

    public boolean updatePrice(int vehicleId, double price)
            throws SQLException {
        if (vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle ID.");
        }

        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException(
                    "Price must be a positive amount."
            );
        }

        return vehicleDAO.updatePrice(vehicleId, price);
    }

    public boolean updateStatus(int vehicleId, String status)
            throws SQLException {
        if (vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle ID.");
        }

        if (status == null
                || !(status.equalsIgnoreCase("AVAILABLE")
                || status.equalsIgnoreCase("RENTED")
                || status.equalsIgnoreCase("MAINTENANCE"))) {
            throw new IllegalArgumentException(
                    "Status must be AVAILABLE, RENTED or MAINTENANCE."
            );
        }

        return vehicleDAO.updateStatus(
                vehicleId, status.toUpperCase()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
