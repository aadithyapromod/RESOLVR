package com.resolvr.service;

import com.resolvr.dao.CustomerDAO;
import com.resolvr.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    // Register a new customer
    public boolean addCustomer(Customer customer) throws SQLException {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (isBlank(customer.getName())) {
            throw new IllegalArgumentException("Customer name is required.");
        }

        if (isBlank(customer.getLicenseNo())) {
            throw new IllegalArgumentException(
                    "Driving licence number is required."
            );
        }

        if (!isBlank(customer.getPhone())
                && !customer.getPhone().trim().matches("[0-9+() -]{7,20}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number."
            );
        }

        if (!isBlank(customer.getEmail())
                && !customer.getEmail().trim()
                        .matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address."
            );
        }

        // Check if driving license is already registered
        Customer existingByLic = customerDAO.getCustomerByLicense(customer.getLicenseNo().trim());
        if (existingByLic != null) {
            throw new IllegalArgumentException("A customer with this licence number already exists.");
        }

        return customerDAO.addCustomer(customer);
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    // Retrieve a customer by ID
    public Customer getCustomerById(int customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        return customerDAO.getCustomerById(customerId);
    }

    // Search by driving license
    public Customer getCustomerByLicense(String licenseNo) throws SQLException {
        if (isBlank(licenseNo)) {
            return null;
        }
        return customerDAO.getCustomerByLicense(licenseNo.trim());
    }

    // Search by email
    public Customer getCustomerByEmail(String email) throws SQLException {
        if (isBlank(email)) {
            return null;
        }
        return customerDAO.getCustomerByEmail(email.trim());
    }

    // Find customer for customer portal login (accepts ID, licence, email, or phone)
    public Customer findCustomerByLogin(String loginInput) throws SQLException {
        if (isBlank(loginInput)) {
            return null;
        }

        String query = loginInput.trim();

        // 1. Try customer ID if integer
        if (query.matches("^\\d+$")) {
            try {
                int id = Integer.parseInt(query);
                Customer c = customerDAO.getCustomerById(id);
                if (c != null) return c;
            } catch (NumberFormatException ignored) {}
        }

        // 2. Try license number
        Customer c = customerDAO.getCustomerByLicense(query);
        if (c != null) return c;

        // 3. Try email
        c = customerDAO.getCustomerByEmail(query);
        if (c != null) return c;

        // 4. Try phone
        c = customerDAO.getCustomerByPhone(query);
        return c;
    }

    // Update customer details
    public boolean updateCustomer(Customer customer) throws SQLException {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        if (isBlank(customer.getName())
                || isBlank(customer.getLicenseNo())) {
            throw new IllegalArgumentException(
                    "Customer name and licence number are required."
            );
        }

        if (!isBlank(customer.getPhone())
                && !customer.getPhone().trim().matches("[0-9+() -]{7,20}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number."
            );
        }

        if (!isBlank(customer.getEmail())
                && !customer.getEmail().trim()
                        .matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address."
            );
        }

        return customerDAO.updateCustomer(customer);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
