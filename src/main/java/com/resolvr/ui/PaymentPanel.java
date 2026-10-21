
package com.resolvr.ui;

import com.resolvr.model.Payment;
import com.resolvr.service.PaymentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PaymentPanel extends JPanel {
    private final PaymentService service = new PaymentService();

    private final JTextField rentalId = new JTextField();
    private final JTextField amount = new JTextField();

    private final JComboBox<String> method = new JComboBox<>(
            new String[]{"CASH", "CARD", "UPI"});

    private final JComboBox<String> paymentStatus = new JComboBox<>(
            new String[]{"PENDING", "PAID", "FAILED", "REFUNDED"});

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{
                    "Payment ID", "Rental ID", "Amount",
                    "Method", "Status", "Transaction Date"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

    private final JTable table = new JTable(tableModel);

    public PaymentPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Payment Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Payment Details"));

        form.add(new JLabel("Rental ID"));
        form.add(rentalId);
        form.add(new JLabel("Amount (₹)"));
        form.add(amount);
        form.add(new JLabel("Payment Method"));
        form.add(method);
        form.add(new JLabel("Payment Status"));
        form.add(paymentStatus);

        JButton add = new JButton("Record Payment");
        JButton refresh = new JButton("Refresh");
        JButton filter = new JButton("Search by Rental ID");
        JButton update = new JButton("Update Selected Status");

        form.add(add);
        form.add(refresh);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(filter);
        controls.add(update);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(form, BorderLayout.CENTER);
        top.add(controls, BorderLayout.SOUTH);

        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(top, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        add.addActionListener(e -> addPayment());
        refresh.addActionListener(e -> loadPayments());
        filter.addActionListener(e -> searchPayments());
        update.addActionListener(e -> updateStatus());

        loadPayments();
    }

    private void addPayment() {
        try {
            int rental = Integer.parseInt(rentalId.getText().trim());
            double value = Double.parseDouble(amount.getText().trim());

            Payment payment = new Payment(
                    rental, value,
                    (String) method.getSelectedItem(),
                    (String) paymentStatus.getSelectedItem()
            );

            if (service.addPayment(payment)) {
                message("Payment recorded successfully.");
                amount.setText("");
                loadPayments();
            } else {
                message("Payment could not be recorded.");
            }
        } catch (NumberFormatException ex) {
            message("Enter a valid rental ID and amount.");
        } catch (SQLException | IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void loadPayments() {
        try {
            display(service.getAllPayments());
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void searchPayments() {
        String input = JOptionPane.showInputDialog(
                this, "Enter the rental ID:");

        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            display(service.getPaymentsByRentalId(id));
        } catch (NumberFormatException ex) {
            message("Enter a valid rental ID.");
        } catch (SQLException | IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void display(List<Payment> payments) {
        tableModel.setRowCount(0);

        for (Payment p : payments) {
            tableModel.addRow(new Object[]{
                    p.getPaymentId(), p.getRentalId(),
                    p.getAmount(), p.getMethod(),
                    p.getPaymentStatus(), p.getTransactionDate()
            });
        }
    }

    private void updateStatus() {
        int row = table.getSelectedRow();

        if (row < 0) {
            message("Select a payment from the table first.");
            return;
        }

        int id = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString());

        String newStatus = (String) paymentStatus.getSelectedItem();

        try {
            if (service.updatePaymentStatus(id, newStatus)) {
                message("Payment status updated.");
                loadPayments();
            } else {
                message("Payment status was not updated.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
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
