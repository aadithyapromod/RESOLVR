
package com.resolvr;

import com.resolvr.dao.VehicleDAO;
import com.resolvr.model.Vehicle;

public class VehicleDAOTest {

    public static void main(String[] args) {

        VehicleDAO vehicleDAO = new VehicleDAO();

        try {
            Vehicle vehicle = new Vehicle(
                "KL02AB1234",
                "Honda Activa 125",
                "Scooter",
                800.00
            );

            boolean added = vehicleDAO.addVehicle(vehicle);

            if (added) {
                System.out.println("Vehicle added successfully!");
            }

            System.out.println("\nVehicles in RESOLVR:");

            for (Vehicle v : vehicleDAO.getAllVehicles()) {
                System.out.println(
                    v.getVehicleId() + " | " +
                    v.getRegistrationNo() + " | " +
                    v.getModel() + " | " +
                    v.getCategory() + " | Rs." +
                    v.getPricePerDay() + " | " +
                    v.getStatus()
                );
            }

        } catch (Exception e) {
            System.out.println("Test failed:");
            e.printStackTrace();
        }
    }
}

