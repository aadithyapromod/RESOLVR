package com.resolvr.ui;

import com.resolvr.model.Customer;
import com.resolvr.service.CustomerService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CustomerLoginFrame extends JFrame {

    private final CustomerService customerService = new CustomerService();

    // Login form fields
    private final JTextField loginInput = new JTextField(18);

    // Registration form fields
    private final JTextField regName = new JTextField(18);
    private final JTextField regPhone = new JTextField(18);
    private final JTextField regEmail = new JTextField(18);
    private final JTextField regLicense = new JTextField(18);

    public CustomerLoginFrame() {
        setTitle("RESOLVR - Customer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 560);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(239, 243, 249));

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 233)),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        // Top Brand Header
        JPanel brandPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        brandPanel.setOpaque(false);

        JLabel title = new JLabel("RESOLVR");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(new Color(29, 42, 65));

        JLabel subtitle = new JLabel("Customer Portal - Rent a Vehicle");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        brandPanel.add(title);
        brandPanel.add(subtitle);
        card.add(brandPanel, BorderLayout.NORTH);

        // Center Tabs: Login or Register
        JTabbedPane authTabs = new JTabbedPane();

        // TAB 1: Existing Customer Login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel loginInstruction = new JLabel("Enter your Driving Licence, Email, or Customer ID:");
        loginInstruction.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginInstruction.setForeground(new Color(70, 75, 85));
        gbc.gridwidth = 2;
        loginPanel.add(loginInstruction, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        loginPanel.add(new JLabel("Credential:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(loginInput, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("Enter Customer Portal");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(39, 85, 145));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton, gbc);

        gbc.gridy++;
        JLabel loginTip = new JLabel("Demo: You can use licence 'TESTLICENSE001' or register a new account");
        loginTip.setHorizontalAlignment(SwingConstants.CENTER);
        loginTip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        loginTip.setForeground(Color.GRAY);
        loginPanel.add(loginTip, gbc);

        authTabs.addTab("Existing Customer", loginPanel);

        // TAB 2: New Customer Registration
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(Color.WHITE);
        GridBagConstraints regGbc = new GridBagConstraints();
        regGbc.insets = new Insets(6, 6, 6, 6);
        regGbc.fill = GridBagConstraints.HORIZONTAL;
        regGbc.gridx = 0;
        regGbc.gridy = 0;

        regGbc.gridwidth = 2;
        JLabel regInstruction = new JLabel("New to RESOLVR? Register below to start renting:");
        regInstruction.setFont(new Font("SansSerif", Font.PLAIN, 12));
        regInstruction.setForeground(new Color(70, 75, 85));
        registerPanel.add(regInstruction, regGbc);

        regGbc.gridy++;
        regGbc.gridwidth = 1;
        registerPanel.add(new JLabel("Full Name:"), regGbc);
        regGbc.gridx = 1;
        registerPanel.add(regName, regGbc);

        regGbc.gridy++;
        regGbc.gridx = 0;
        registerPanel.add(new JLabel("Phone:"), regGbc);
        regGbc.gridx = 1;
        registerPanel.add(regPhone, regGbc);

        regGbc.gridy++;
        regGbc.gridx = 0;
        registerPanel.add(new JLabel("Email:"), regGbc);
        regGbc.gridx = 1;
        registerPanel.add(regEmail, regGbc);

        regGbc.gridy++;
        regGbc.gridx = 0;
        registerPanel.add(new JLabel("Licence No:"), regGbc);
        regGbc.gridx = 1;
        registerPanel.add(regLicense, regGbc);

        regGbc.gridy++;
        regGbc.gridx = 0;
        regGbc.gridwidth = 2;

        JButton registerButton = new JButton("Register & Start Renting");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setBackground(new Color(39, 85, 145));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerPanel.add(registerButton, regGbc);

        authTabs.addTab("New Customer Registration", registerPanel);

        card.add(authTabs, BorderLayout.CENTER);

        // Footer: Switch to Staff Login
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JButton switchToStaff = new JButton("Staff Login →");
        switchToStaff.setFont(new Font("SansSerif", Font.PLAIN, 12));
        switchToStaff.setForeground(new Color(39, 85, 145));
        switchToStaff.setContentAreaFilled(false);
        switchToStaff.setBorderPainted(false);
        switchToStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.add(new JLabel("Are you an employee?"));
        footer.add(switchToStaff);

        card.add(footer, BorderLayout.SOUTH);

        background.add(card);
        add(background);

        // Action Handlers
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());

        switchToStaff.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin() {
        String input = loginInput.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your Driving Licence, Email, or Customer ID.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Customer customer = customerService.findCustomerByLogin(input);
            if (customer != null) {
                dispose();
                new CustomerDashboardFrame(customer).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No customer found matching '" + input + "'.\n" +
                        "If you are a new customer, please use the 'New Customer Registration' tab.",
                        "Customer Not Found",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleRegistration() {
        String name = regName.getText().trim();
        String phone = regPhone.getText().trim();
        String email = regEmail.getText().trim();
        String license = regLicense.getText().trim();

        if (name.isEmpty() || license.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Name and Driving Licence number are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Customer newCustomer = new Customer(name, phone, email, license);

        try {
            if (customerService.addCustomer(newCustomer)) {
                // Fetch the complete customer record to ensure ID is populated
                Customer created = customerService.getCustomerByLicense(license);
                if (created == null) created = newCustomer;

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome to RESOLVR, " + created.getName() + "!\n" +
                        "Your Customer ID is #" + created.getCustomerId() + ".\n" +
                        "Proceeding to vehicle rentals...",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
                new CustomerDashboardFrame(created).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not complete registration. Please try again.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new CustomerLoginFrame().setVisible(true);
        });
    }
}
