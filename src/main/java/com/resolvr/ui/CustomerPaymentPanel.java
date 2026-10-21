package com.resolvr.ui;

import com.resolvr.model.Customer;
import com.resolvr.model.Payment;
import com.resolvr.model.Rental;
import com.resolvr.service.PaymentService;
import com.resolvr.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPaymentPanel extends JPanel {

    private final Customer customer;
    private final PaymentService paymentService = new PaymentService();
    private final RentalService rentalService = new RentalService();

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{
                    "Payment ID", "Rental ID", "Amount (₹)",
                    "Method", "Status", "Transaction Date"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

    private final JTable table = new JTable(tableModel);

    public CustomerPaymentPanel(Customer customer) {
        this.customer = customer;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("My Payments");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Review invoices and pay for your rental bookings.");
        subtitle.setForeground(Color.GRAY);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton refreshBtn = new JButton("Refresh");
        JButton payBtn = new JButton("Pay Selected Bill");
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        payBtn.setBackground(new Color(39, 85, 145));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);

        controls.add(refreshBtn);
        controls.add(payBtn);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(controls, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> loadPayments());
        payBtn.addActionListener(e -> payBill());

        loadPayments();
    }

    public void loadPayments() {
        try {
            List<Payment> payments = paymentService.getPaymentsByCustomerId(customer.getCustomerId());
            tableModel.setRowCount(0);

            for (Payment p : payments) {
                tableModel.addRow(new Object[]{
                        p.getPaymentId(),
                        p.getRentalId(),
                        String.format("%.2f", p.getAmount()),
                        p.getMethod(),
                        p.getPaymentStatus(),
                        p.getTransactionDate() != null ? p.getTransactionDate().toString() : "Pending"
                });
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void payBill() {
        int row = table.getSelectedRow();
        if (row < 0) {
            message("Select a payment record from the table first.");
            return;
        }

        int paymentId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int rentalId = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
        String amount = tableModel.getValueAt(row, 2).toString();
        String currentStatus = tableModel.getValueAt(row, 4).toString();

        if ("PAID".equalsIgnoreCase(currentStatus)) {
            message("This payment has already been completed.");
            return;
        }

        String[] methods = {"UPI", "CARD", "CASH"};
        String selectedMethod = (String) JOptionPane.showInputDialog(
                this,
                "Payment Amount: ₹" + amount + "\nRental ID: #" + rentalId + "\n\nSelect Payment Method:",
                "Process Payment",
                JOptionPane.QUESTION_MESSAGE,
                null,
                methods,
                methods[0]
        );

        if (selectedMethod == null) {
            return;
        }

        try {
            if (paymentService.updatePaymentStatus(paymentId, "PAID")) {
                // If rental was BOOKED, activate it upon payment
                Rental rental = rentalService.getRentalById(rentalId);
                if (rental != null && "BOOKED".equalsIgnoreCase(rental.getStatus())) {
                    rentalService.updateRentalStatus(rentalId, "ACTIVE");
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Payment Successful!\n\n" +
                        "Payment ID: #" + paymentId + "\n" +
                        "Rental ID: #" + rentalId + "\n" +
                        "Amount Paid: ₹" + amount + "\n" +
                        "Method: " + selectedMethod + "\n" +
                        "Status: PAID\n\n" +
                        "Your rental is now ACTIVE. Enjoy your ride!",
                        "Payment Receipt",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadPayments();
            } else {
                message("Failed to process payment.");
            }
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
