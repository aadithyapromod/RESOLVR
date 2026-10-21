package com.resolvr;

import com.resolvr.dao.CustomerDAO;
import com.resolvr.model.Customer;

public class CustomerDAOTest {

    public static void main(String[] args) {

        CustomerDAO customerDAO = new CustomerDAO();

        try {
            System.out.println("\nCustomers in RESOLVR:");

            for (Customer c : customerDAO.getAllCustomers()) {
                System.out.println(
                    c.getCustomerId() + " | " +
                    c.getName() + " | " +
                    c.getPhone() + " | " +
                    c.getEmail() + " | " +
                    c.getLicenseNo()
                );
            }

        } catch (Exception e) {
            System.out.println("Test failed:");
            e.printStackTrace();
        }
    }
}
