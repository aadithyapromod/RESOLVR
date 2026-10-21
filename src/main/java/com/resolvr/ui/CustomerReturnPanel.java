package com.resolvr.ui;

import com.resolvr.model.Customer;
import com.resolvr.model.Rental;
import com.resolvr.model.Vehicle;
import com.resolvr.model.VehicleReturn;
import com.resolvr.service.RentalService;
import com.resolvr.service.ReturnService;
import com.resolvr.service.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerReturnPanel extends JPanel {

    private final Customer customer;
    private final ReturnService returnService = new ReturnService();
    private final RentalService rentalService = new RentalService();
    private final VehicleService vehicleService = new VehicleService();

    private final JComboBox<RentalItem> activeRentalsCombo = new JComboBox<>();
    private final JTextField returnDateField = new JTextField(LocalDate.now().toString());
    private final JTextField notesField = new JTextField("Vehicle returned in good condition");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{
                    "Return ID", "Rental ID", "Vehicle",
                    "Return Date", "Damage Charge (₹)", "Refund (₹)", "Condition / Remarks"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

    private final JTable table = new JTable(tableModel);

    // Helper item to display rental info nicely in JComboBox
    private static class RentalItem {
        final Rental rental;
        final String displayText;

        RentalItem(Rental rental, String displayText) {
            this.rental = rental;
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    public CustomerReturnPanel(Customer customer) {
        this.customer = customer;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Vehicle Returns");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Return your currently rented vehicle and view past return inspection receipts.");
        subtitle.setForeground(Color.GRAY);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Top Return Form
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Return a Rented Vehicle"),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        form.add(new JLabel("Active Rental:"));
        form.add(activeRentalsCombo);

        form.add(new JLabel("Actual Return Date (YYYY-MM-DD):"));
        form.add(returnDateField);

        form.add(new JLabel("Vehicle Condition / Notes:"));
        form.add(notesField);

        JButton returnButton = new JButton("Submit Vehicle Return");
        returnButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        returnButton.setBackground(new Color(39, 85, 145));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);

        JButton refreshButton = new JButton("Refresh");

        form.add(returnButton);
        form.add(refreshButton);

        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createTitledBorder("My Past Return Records"));
        content.add(form, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        returnButton.addActionListener(e -> handleVehicleReturn());
        refreshButton.addActionListener(e -> {
            loadActiveRentals();
            loadReturnHistory();
        });

        loadActiveRentals();
        loadReturnHistory();
    }

    public void loadActiveRentals() {
        activeRentalsCombo.removeAllItems();
        try {
            List<Rental> allRentals = rentalService.getRentalsByCustomerId(customer.getCustomerId());
            int count = 0;
            for (Rental r : allRentals) {
                if ("ACTIVE".equalsIgnoreCase(r.getStatus()) || "BOOKED".equalsIgnoreCase(r.getStatus())) {
                    String vName = "Vehicle #" + r.getVehicleId();
                    try {
                        Vehicle v = vehicleService.getVehicleById(r.getVehicleId());
                        if (v != null) {
                            vName = v.getModel() + " (" + v.getRegistrationNo() + ")";
                        }
                    } catch (SQLException ignored) {}

                    String text = "Rental #" + r.getRentalId() + " - " + vName + " [" + r.getStatus() + "]";
                    activeRentalsCombo.addItem(new RentalItem(r, text));
                    count++;
                }
            }

            if (count == 0) {
                activeRentalsCombo.addItem(new RentalItem(null, "No active rentals to return"));
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    public void loadReturnHistory() {
        try {
            List<VehicleReturn> returns = returnService.getReturnsByCustomerId(customer.getCustomerId());
            tableModel.setRowCount(0);

            for (VehicleReturn vr : returns) {
                String vehicleName = "Rental #" + vr.getRentalId();
                try {
                    Rental r = rentalService.getRentalById(vr.getRentalId());
                    if (r != null) {
                        Vehicle v = vehicleService.getVehicleById(r.getVehicleId());
                        if (v != null) {
                            vehicleName = v.getModel() + " (" + v.getRegistrationNo() + ")";
                        }
                    }
                } catch (SQLException ignored) {}

                tableModel.addRow(new Object[]{
                        vr.getReturnId(),
                        vr.getRentalId(),
                        vehicleName,
                        vr.getActualReturnDate(),
                        String.format("%.2f", vr.getDamageCharge()),
                        String.format("%.2f", vr.getRefundAmount()),
                        vr.getConditionNotes()
                });
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void handleVehicleReturn() {
        RentalItem selected = (RentalItem) activeRentalsCombo.getSelectedItem();
        if (selected == null || selected.rental == null) {
            message("You do not have any active rental selected to return.");
            return;
        }

        Rental rental = selected.rental;
        String dateText = returnDateField.getText().trim();
        String notes = notesField.getText().trim();

        if (dateText.isEmpty()) {
            message("Return date is required.");
            return;
        }

        try {
            Date returnDate = Date.valueOf(dateText);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm Vehicle Return:\n\n" +
                    "Rental ID: #" + rental.getRentalId() + "\n" +
                    "Return Date: " + returnDate + "\n" +
                    "Condition Remarks: " + notes + "\n\n" +
                    "Do you want to submit this return?",
                    "Confirm Return",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            VehicleReturn vr = new VehicleReturn(
                    rental.getRentalId(),
                    returnDate,
                    0.0,
                    0.0,
                    notes.isEmpty() ? "Vehicle returned by customer" : notes
            );

            if (returnService.processReturn(vr)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle Returned Successfully!\n\n" +
                        "Rental #" + rental.getRentalId() + " has been marked COMPLETED.\n" +
                        "Thank you for returning the vehicle!",
                        "Return Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadActiveRentals();
                loadReturnHistory();
            } else {
                message("Could not process vehicle return. Please try again.");
            }

        } catch (IllegalArgumentException ex) {
            message("Invalid date format. Use YYYY-MM-DD.\n" + ex.getMessage());
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
