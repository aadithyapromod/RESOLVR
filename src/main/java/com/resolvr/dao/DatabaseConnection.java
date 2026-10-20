
package com.resolvr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/resolvr";

    private static final String USER = "aadi";

    // Replace this with your actual MySQL password
    private static final String PASSWORD = "8943296569";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("RESOLVR connected to MySQL successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            System.out.println(e.getMessage());
        }
    }
}
