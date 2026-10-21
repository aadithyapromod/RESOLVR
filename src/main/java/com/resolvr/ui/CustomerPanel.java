package com.resolvr.ui;

import com.resolvr.model.Customer;
import com.resolvr.service.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final CustomerService service = new CustomerService();

    private final JTextField name = new JTextField();
    private final JTextField phone = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField license = new JTextField();

    private int selectedCustomerId = -1;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{
                    "ID", "Name", "Phone", "Email", "Licence Number"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

    private final JTable table = new JTable(tableModel);

    public CustomerPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Customer Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        form.add(new JLabel("Name"));
        form.add(name);
        form.add(new JLabel("Phone"));
        form.add(phone);
        form.add(new JLabel("Email"));
        form.add(email);
        form.add(new JLabel("Driving licence number"));
        form.add(license);

        JButton add = new JButton("Add Customer");
        JButton update = new JButton("Update Selected");
        JButton refresh = new JButton("Refresh");
        JButton clear = new JButton("Clear Form");

        form.add(add);
        form.add(update);
        form.add(refresh);
        form.add(clear);

        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(form, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        add.addActionListener(e -> addCustomer());
        update.addActionListener(e -> updateCustomer());
        refresh.addActionListener(e -> loadCustomers());
        clear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                selectedCustomerId = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString());

                name.setText(cell(row, 1));
                phone.setText(cell(row, 2));
                email.setText(cell(row, 3));
                license.setText(cell(row, 4));
            }
        });

        loadCustomers();
    }

    private String cell(int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private Customer readForm() {
        return new Customer(name.getText().trim(),
                phone.getText().trim(),
                email.getText().trim(),
                license.getText().trim());
    }

    private void addCustomer() {
        try {
            if (service.addCustomer(readForm())) {
                message("Customer added successfully.");
                clearForm();
                loadCustomers();
            } else {
                message("Customer could not be added.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void updateCustomer() {
        if (selectedCustomerId < 1) {
            message("Select a customer from the table first.");
            return;
        }

        try {
            Customer customer = readForm();
            Customer updated = new Customer(
                    selectedCustomerId, customer.getName(),
                    customer.getPhone(), customer.getEmail(),
                    customer.getLicenseNo());

            if (service.updateCustomer(updated)) {
                message("Customer updated successfully.");
                clearForm();
                loadCustomers();
            } else {
                message("Customer was not updated.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = service.getAllCustomers();
            tableModel.setRowCount(0);

            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                        c.getCustomerId(), c.getName(), c.getPhone(),
                        c.getEmail(), c.getLicenseNo()
                });
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void clearForm() {
        name.setText("");
        phone.setText("");
        email.setText("");
        license.setText("");
        selectedCustomerId = -1;
        table.clearSelection();
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}