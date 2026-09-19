package com.vehiclerental.dao;

import com.vehiclerental.model.Customer;
import com.vehiclerental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE
    public void addCustomer(Customer customer) throws SQLException {

        String sql = """
                INSERT INTO customers
                (name, phone, email, license_number)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNumber());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    customer.setCustomerId(generatedId);
                }
            }
        }
    }


    // READ - Get customer by ID
    public Customer getCustomerById(int customerId) throws SQLException {

        String sql = """
                SELECT *
                FROM customers
                WHERE customer_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Customer customer = new Customer(
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("email"),
                            resultSet.getString("license_number")
                    );

                    customer.setCustomerId(
                            resultSet.getInt("customer_id")
                    );

                    return customer;
                }
            }
        }

        return null;
    }


    // READ - Get all customers
    public List<Customer> getAllCustomers() throws SQLException {

        String sql = """
                SELECT *
                FROM customers
                """;

        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("license_number")
                );

                customer.setCustomerId(
                        resultSet.getInt("customer_id")
                );

                customers.add(customer);
            }
        }

        return customers;
    }


    // UPDATE
    public boolean updateCustomer(Customer customer) throws SQLException {

        String sql = """
                UPDATE customers
                SET name = ?,
                    phone = ?,
                    email = ?,
                    license_number = ?
                WHERE customer_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getLicenseNumber());
            statement.setInt(5, customer.getCustomerId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // DELETE
    public boolean deleteCustomer(int customerId) throws SQLException {

        String sql = """
                DELETE FROM customers
                WHERE customer_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}