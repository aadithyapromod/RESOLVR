
package com.resolvr.service;

import com.resolvr.dao.RentalDAO;
import com.resolvr.dao.ReturnDAO;
import com.resolvr.model.Rental;
import com.resolvr.model.VehicleReturn;

import java.sql.SQLException;
import java.util.List;

public class ReturnService {

    private final ReturnDAO returnDAO;
    private final RentalDAO rentalDAO;

    public ReturnService() {
        this.returnDAO = new ReturnDAO();
        this.rentalDAO = new RentalDAO();
    }

    // Record a vehicle return
    public boolean addReturn(VehicleReturn vehicleReturn)
            throws SQLException {

        if (vehicleReturn == null) {
            throw new IllegalArgumentException(
                    "Return record cannot be null."
            );
        }

        if (vehicleReturn.getRentalId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid rental ID is required."
            );
        }

        if (vehicleReturn.getActualReturnDate() == null) {
            throw new IllegalArgumentException(
                    "Actual return date is required."
            );
        }

        if (!Double.isFinite(vehicleReturn.getDamageCharge())
                || vehicleReturn.getDamageCharge() < 0) {
            throw new IllegalArgumentException(
                    "Damage charge cannot be negative."
            );
        }

        if (!Double.isFinite(vehicleReturn.getRefundAmount())
                || vehicleReturn.getRefundAmount() < 0) {
            throw new IllegalArgumentException(
                    "Refund amount cannot be negative."
            );
        }

        Rental rental =
                rentalDAO.getRentalById(vehicleReturn.getRentalId());

        if (rental == null) {
            throw new IllegalArgumentException(
                    "The specified rental does not exist."
            );
        }

        if ("COMPLETED".equalsIgnoreCase(rental.getStatus())
                || "CANCELLED".equalsIgnoreCase(rental.getStatus())
                || "RETURNED".equalsIgnoreCase(rental.getStatus())) {
            throw new IllegalArgumentException(
                    "This rental is already completed, cancelled or returned."
            );
        }

        return returnDAO.addReturn(vehicleReturn);
    }

    // Retrieve all return records
    public List<VehicleReturn> getAllReturns() throws SQLException {
        return returnDAO.getAllReturns();
    }

    // Retrieve a return record by rental ID
    public VehicleReturn getReturnByRentalId(int rentalId)
            throws SQLException {

        if (rentalId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid rental ID."
            );
        }

        return returnDAO.getReturnByRentalId(rentalId);
    }
}
