package com.resolvr.ui;

import com.resolvr.model.Customer;
import com.resolvr.model.Payment;
import com.resolvr.model.Rental;
import com.resolvr.model.Vehicle;
import com.resolvr.service.PaymentService;
import com.resolvr.service.RentalService;
import com.resolvr.service.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CustomerRentVehiclePanel extends JPanel {

    private final Customer customer;
    private final VehicleService vehicleService = new VehicleService();
    private final RentalService rentalService = new RentalService();
    private final PaymentService paymentService = new PaymentService();

    private final JComboBox<String> categoryFilter = new JComboBox<>(
            new String[]{"ALL", "Car", "SUV", "Van", "Bike", "Other"}
    );

    private final DefaultTableModel vehicleTableModel =
            new DefaultTableModel(new String[]{
                    "Vehicle ID", "Registration", "Model",
                    "Category", "Hourly Price (₹/hr)", "Status"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

    private final JTable vehicleTable = new JTable(vehicleTableModel);

    // Booking form controls
    private final JLabel selectedVehicleLabel = new JLabel("None selected (click a vehicle above)");
    private final JTextField startDateField = new JTextField(LocalDate.now().toString());
    private final JTextField endDateField = new JTextField(LocalDate.now().toString());
    private final JSpinner hoursSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 720, 1));
    private final JLabel estimatedTotalLabel = new JLabel("₹0.00");
    private final JComboBox<String> paymentMethodBox = new JComboBox<>(new String[]{"UPI", "CARD", "CASH"});

    private Vehicle selectedVehicle = null;

    public CustomerRentVehiclePanel(Customer customer) {
        this.customer = customer;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // 1. Header banner
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Browse & Rent Vehicles");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel hint = new JLabel("Select an available vehicle inputted by staff, choose your rental hours, and book instantly.");
        hint.setForeground(Color.GRAY);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(hint, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Center: Vehicle Browser Table with Filter
        JPanel tableContainer = new JPanel(new BorderLayout(8, 8));
        tableContainer.setBorder(BorderFactory.createTitledBorder("Available Vehicles"));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.add(new JLabel("Filter by Category:"));
        filterBar.add(categoryFilter);
        JButton filterBtn = new JButton("Filter");
        JButton refreshBtn = new JButton("Refresh");
        filterBar.add(filterBtn);
        filterBar.add(refreshBtn);
        tableContainer.add(filterBar, BorderLayout.NORTH);

        vehicleTable.setRowHeight(25);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableContainer.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        // 3. Bottom: Booking Card
        JPanel bookingCard = new JPanel(new BorderLayout(10, 10));
        bookingCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Book Selected Vehicle"),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 10, 8));

        formGrid.add(new JLabel("Selected Vehicle:"));
        selectedVehicleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        selectedVehicleLabel.setForeground(new Color(29, 42, 65));
        formGrid.add(selectedVehicleLabel);

        formGrid.add(new JLabel("Renting as:"));
        JLabel customerLabel = new JLabel(customer.getName() + " (Licence: " + customer.getLicenseNo() + ")");
        customerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        formGrid.add(customerLabel);

        formGrid.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formGrid.add(startDateField);

        formGrid.add(new JLabel("End Date (YYYY-MM-DD):"));
        formGrid.add(endDateField);

        formGrid.add(new JLabel("Rental Duration (Hours):"));
        formGrid.add(hoursSpinner);

        formGrid.add(new JLabel("Payment Method:"));
        formGrid.add(paymentMethodBox);

        formGrid.add(new JLabel("Estimated Total Amount:"));
        estimatedTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        estimatedTotalLabel.setForeground(new Color(39, 85, 145));
        formGrid.add(estimatedTotalLabel);

        JButton bookButton = new JButton("Confirm & Book Vehicle");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookButton.setBackground(new Color(39, 85, 145));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);

        JButton recalculateButton = new JButton("Recalculate Cost");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(recalculateButton);
        buttonPanel.add(bookButton);

        bookingCard.add(formGrid, BorderLayout.CENTER);
        bookingCard.add(buttonPanel, BorderLayout.SOUTH);

        // Split view or combined view
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableContainer, bookingCard);
        splitPane.setResizeWeight(0.55);
        splitPane.setDividerSize(6);
        add(splitPane, BorderLayout.CENTER);

        // Listeners
        filterBtn.addActionListener(e -> filterVehicles());
        refreshBtn.addActionListener(e -> loadAvailableVehicles());
        recalculateButton.addActionListener(e -> updateEstimate());
        hoursSpinner.addChangeListener(e -> updateEstimate());

        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vehicleTable.getSelectedRow() >= 0) {
                int row = vehicleTable.getSelectedRow();
                int vehicleId = Integer.parseInt(vehicleTableModel.getValueAt(row, 0).toString());
                try {
                    selectedVehicle = vehicleService.getVehicleById(vehicleId);
                    if (selectedVehicle != null) {
                        selectedVehicleLabel.setText(
                                selectedVehicle.getModel() + " [" + selectedVehicle.getRegistrationNo() +
                                "] (" + selectedVehicle.getCategory() + ") - ₹" +
                                String.format("%.2f", selectedVehicle.getPricePerHour()) + "/hr"
                        );
                        updateEstimate();
                    }
                } catch (SQLException ex) {
                    error(ex);
                }
            }
        });

        bookButton.addActionListener(e -> bookRental());

        loadAvailableVehicles();
    }

    private void updateEstimate() {
        if (selectedVehicle == null) {
            estimatedTotalLabel.setText("₹0.00");
            return;
        }

        try {
            int hours = ((Number) hoursSpinner.getValue()).intValue();
            if (hours <= 0) hours = 1;
            double total = hours * selectedVehicle.getPricePerHour();
            estimatedTotalLabel.setText(String.format("₹%.2f (%d hrs @ ₹%.2f/hr)",
                    total, hours, selectedVehicle.getPricePerHour()));
        } catch (Exception ex) {
            estimatedTotalLabel.setText("₹0.00");
        }
    }

    public void loadAvailableVehicles() {
        try {
            List<Vehicle> available = vehicleService.getAvailableVehicles();
            display(available);
            selectedVehicle = null;
            selectedVehicleLabel.setText("None selected (click a vehicle above)");
            estimatedTotalLabel.setText("₹0.00");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void filterVehicles() {
        String cat = (String) categoryFilter.getSelectedItem();
        if ("ALL".equals(cat)) {
            loadAvailableVehicles();
            return;
        }

        try {
            List<Vehicle> vehicles = vehicleService.searchByCategory(cat);
            // Filter to only show available ones
            List<Vehicle> available = vehicles.stream()
                    .filter(v -> "AVAILABLE".equalsIgnoreCase(v.getStatus()))
                    .toList();
            display(available);
            selectedVehicle = null;
            selectedVehicleLabel.setText("None selected (click a vehicle above)");
            estimatedTotalLabel.setText("₹0.00");
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void display(List<Vehicle> vehicles) {
        vehicleTableModel.setRowCount(0);
        for (Vehicle v : vehicles) {
            vehicleTableModel.addRow(new Object[]{
                    v.getVehicleId(),
                    v.getRegistrationNo(),
                    v.getModel(),
                    v.getCategory(),
                    String.format("%.2f", v.getPricePerHour()),
                    v.getStatus()
            });
        }
    }

    private void bookRental() {
        if (selectedVehicle == null) {
            message("Please select a vehicle from the table first.");
            return;
        }

        try {
            Date start = Date.valueOf(startDateField.getText().trim());
            Date end = Date.valueOf(endDateField.getText().trim());

            if (end.before(start)) {
                message("End date cannot be earlier than start date.");
                return;
            }

            int hours = ((Number) hoursSpinner.getValue()).intValue();
            if (hours <= 0) {
                message("Duration must be at least 1 hour.");
                return;
            }

            double totalAmount = hours * selectedVehicle.getPricePerHour();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm Booking Details:\n\n" +
                    "Vehicle: " + selectedVehicle.getModel() + " (" + selectedVehicle.getRegistrationNo() + ")\n" +
                    "Duration: " + hours + " hour(s)\n" +
                    "Rate: ₹" + String.format("%.2f", selectedVehicle.getPricePerHour()) + "/hr\n" +
                    "Total Amount: ₹" + String.format("%.2f", totalAmount) + "\n" +
                    "Start Date: " + start + "\n" +
                    "End Date: " + end + "\n\n" +
                    "Do you want to proceed?",
                    "Confirm Rental",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            Rental rental = new Rental(
                    customer.getCustomerId(),
                    selectedVehicle.getVehicleId(),
                    start,
                    end,
                    totalAmount
            );

            if (rentalService.addRental(rental)) {
                // Auto create initial payment record
                String method = (String) paymentMethodBox.getSelectedItem();
                Payment payment = new Payment(
                        rental.getRentalId(),
                        totalAmount,
                        method != null ? method : "UPI",
                        "PENDING"
                );
                paymentService.addPayment(payment);

                JOptionPane.showMessageDialog(
                        this,
                        "Rental booked successfully!\n\n" +
                        "Rental ID: #" + rental.getRentalId() + "\n" +
                        "Total Cost: ₹" + String.format("%.2f", totalAmount) + "\n" +
                        "Status: BOOKED\n\n" +
                        "You can review your booking in 'My Bookings' and complete payment in 'My Payments'.",
                        "Booking Confirmed",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadAvailableVehicles();
            } else {
                message("Could not create rental booking. Please try again.");
            }

        } catch (IllegalArgumentException ex) {
            message("Invalid date format. Please use YYYY-MM-DD.\n" + ex.getMessage());
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
