package com.vehiclerental.service;

import com.vehiclerental.dao.CustomerDAO;
import com.vehiclerental.exception.CustomerNotFoundException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }


    // =========================================================
    // ADD CUSTOMER
    // =========================================================

    public void addCustomer(Customer customer)
            throws SQLException {

        validateCustomer(customer);

        customerDAO.addCustomer(customer);
    }


    // =========================================================
    // GET CUSTOMER
    // =========================================================

    public Customer getCustomerById(int customerId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                customerId,
                "Customer ID"
        );

        Customer customer =
                customerDAO.getCustomerById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer with ID " +
                            customerId +
                            " was not found."
            );
        }

        return customer;
    }


    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    public List<Customer> getAllCustomers()
            throws SQLException {

        return customerDAO.getAllCustomers();
    }


    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    public boolean updateCustomer(Customer customer)
            throws SQLException {

        validateCustomer(customer);

        ValidationUtil.requirePositiveId(
                customer.getCustomerId(),
                "Customer ID"
        );

        boolean updated =
                customerDAO.updateCustomer(customer);

        if (!updated) {
            throw new CustomerNotFoundException(
                    "Customer with ID " +
                            customer.getCustomerId() +
                            " was not found."
            );
        }

        return true;
    }


    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    public boolean deleteCustomer(int customerId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                customerId,
                "Customer ID"
        );

        boolean deleted =
                customerDAO.deleteCustomer(customerId);

        if (!deleted) {
            throw new CustomerNotFoundException(
                    "Customer with ID " +
                            customerId +
                            " was not found."
            );
        }

        return true;
    }


    // =========================================================
    // VALIDATE CUSTOMER
    // =========================================================

    private void validateCustomer(
            Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }

        ValidationUtil.requireText(
                customer.getName(),
                "Customer name"
        );

        ValidationUtil.validatePhone(
                customer.getPhone()
        );

        ValidationUtil.validateEmail(
                customer.getEmail()
        );

        ValidationUtil.requireText(
                customer.getLicenseNumber(),
                "License number"
        );
    }
}