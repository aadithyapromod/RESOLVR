package com.resolvr;

import com.resolvr.dao.VehicleDAO;
import com.resolvr.model.Vehicle;

public class VehicleDAOTest {

    public static void main(String[] args) {

        VehicleDAO vehicleDAO = new VehicleDAO();

        try {
            System.out.println("\nVehicles in RESOLVR:");

            for (Vehicle v : vehicleDAO.getAllVehicles()) {
                System.out.println(
                    v.getVehicleId() + " | " +
                    v.getRegistrationNo() + " | " +
                    v.getModel() + " | " +
                    v.getCategory() + " | Rs." +
                    v.getPricePerHour() + "/hr | " +
                    v.getStatus()
                );
            }

        } catch (Exception e) {
            System.out.println("Test failed:");
            e.printStackTrace();
        }
    }
}
