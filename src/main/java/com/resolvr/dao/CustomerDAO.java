package com.resolvr.dao;

import com.resolvr.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Add a customer to the database
    public boolean addCustomer(Customer customer) throws SQLException {

        String sql = "INSERT INTO customers " +
                     "(name, phone, email, license_no) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNo());

            int affected = statement.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        customer.setCustomerId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT customer_id, name, phone, email, license_no " +
                     "FROM customers";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("license_no")
                );

                customers.add(customer);
            }
        }

        return customers;
    }

    // Search for a customer by ID
    public Customer getCustomerById(int customerId) throws SQLException {

        String sql = "SELECT customer_id, name, phone, email, license_no " +
                     "FROM customers WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("license_no")
                    );
                }
            }
        }

        return null;
    }

    // Search for a customer by Licence Number
    public Customer getCustomerByLicense(String licenseNo) throws SQLException {
        if (licenseNo == null) return null;

        String sql = "SELECT customer_id, name, phone, email, license_no " +
                     "FROM customers WHERE LOWER(license_no) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, licenseNo.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("license_no")
                    );
                }
            }
        }

        return null;
    }

    // Search for a customer by Email
    public Customer getCustomerByEmail(String email) throws SQLException {
        if (email == null) return null;

        String sql = "SELECT customer_id, name, phone, email, license_no " +
                     "FROM customers WHERE LOWER(email) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("license_no")
                    );
                }
            }
        }

        return null;
    }

    // Search for a customer by Phone
    public Customer getCustomerByPhone(String phone) throws SQLException {
        if (phone == null) return null;

        String sql = "SELECT customer_id, name, phone, email, license_no " +
                     "FROM customers WHERE phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, phone.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("license_no")
                    );
                }
            }
        }

        return null;
    }

    // Update customer details
    public boolean updateCustomer(Customer customer) throws SQLException {

        String sql = "UPDATE customers SET name = ?, phone = ?, " +
                     "email = ?, license_no = ? WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNo());
            statement.setInt(5, customer.getCustomerId());

            return statement.executeUpdate() > 0;
        }
    }
}
