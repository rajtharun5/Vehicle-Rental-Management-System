package com.vehiclerental.dao;

import com.vehiclerental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    // =========================================================
    // REPORT 1: Available Vehicles
    // =========================================================

    public void showAvailableVehicles() throws SQLException {

        String sql = """
                SELECT vehicle_id,
                       vehicle_number,
                       brand,
                       model,
                       vehicle_type,
                       rental_rate
                FROM vehicles
                WHERE status = 'AVAILABLE'
                ORDER BY brand, model
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println();
            System.out.println("===== AVAILABLE VEHICLES =====");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println("--------------------------------");

                System.out.println("ID: " +
                        resultSet.getInt("vehicle_id"));

                System.out.println("Vehicle Number: " +
                        resultSet.getString("vehicle_number"));

                System.out.println("Brand: " +
                        resultSet.getString("brand"));

                System.out.println("Model: " +
                        resultSet.getString("model"));

                System.out.println("Type: " +
                        resultSet.getString("vehicle_type"));

                System.out.println("Rate: ₹" +
                        resultSet.getDouble("rental_rate"));
            }

            if (!found) {
                System.out.println("No available vehicles.");
            }
        }
    }


    // =========================================================
    // REPORT 2: Active Rentals
    // =========================================================

    public void showActiveRentals() throws SQLException {

        String sql = """
                SELECT
                    r.rental_id,
                    c.name,
                    v.vehicle_number,
                    v.brand,
                    v.model,
                    r.rental_date,
                    r.expected_return_date,
                    r.total_amount
                FROM rentals r
                JOIN customers c
                    ON r.customer_id = c.customer_id
                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id
                WHERE r.status = 'ACTIVE'
                ORDER BY r.rental_date
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println();
            System.out.println("===== ACTIVE RENTALS =====");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println("--------------------------------");

                System.out.println("Rental ID: " +
                        resultSet.getInt("rental_id"));

                System.out.println("Customer: " +
                        resultSet.getString("name"));

                System.out.println("Vehicle: " +
                        resultSet.getString("brand") +
                        " " +
                        resultSet.getString("model"));

                System.out.println("Vehicle Number: " +
                        resultSet.getString("vehicle_number"));

                System.out.println("Rental Date: " +
                        resultSet.getDate("rental_date"));

                System.out.println("Expected Return: " +
                        resultSet.getDate("expected_return_date"));

                System.out.println("Amount: ₹" +
                        resultSet.getDouble("total_amount"));
            }

            if (!found) {
                System.out.println("No active rentals.");
            }
        }
    }


    // =========================================================
    // REPORT 3: Completed Rentals
    // =========================================================

    public void showCompletedRentals() throws SQLException {

        String sql = """
                SELECT
                    r.rental_id,
                    c.name,
                    v.vehicle_number,
                    v.brand,
                    v.model,
                    r.rental_date,
                    r.actual_return_date,
                    r.total_amount
                FROM rentals r
                JOIN customers c
                    ON r.customer_id = c.customer_id
                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id
                WHERE r.status = 'COMPLETED'
                ORDER BY r.actual_return_date DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println();
            System.out.println("===== COMPLETED RENTALS =====");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println("--------------------------------");

                System.out.println("Rental ID: " +
                        resultSet.getInt("rental_id"));

                System.out.println("Customer: " +
                        resultSet.getString("name"));

                System.out.println("Vehicle: " +
                        resultSet.getString("brand") +
                        " " +
                        resultSet.getString("model"));

                System.out.println("Returned On: " +
                        resultSet.getDate("actual_return_date"));

                System.out.println("Amount: ₹" +
                        resultSet.getDouble("total_amount"));
            }

            if (!found) {
                System.out.println("No completed rentals.");
            }
        }
    }


    // =========================================================
    // REPORT 4: Customer Rental History
    // =========================================================

    public void showCustomerRentalHistory(int customerId)
            throws SQLException {

        String sql = """
                SELECT
                    c.name,
                    v.brand,
                    v.model,
                    v.vehicle_number,
                    r.rental_date,
                    r.expected_return_date,
                    r.actual_return_date,
                    r.status,
                    r.total_amount
                FROM rentals r
                JOIN customers c
                    ON r.customer_id = c.customer_id
                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id
                WHERE c.customer_id = ?
                ORDER BY r.rental_date DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                System.out.println();
                System.out.println(
                        "===== CUSTOMER RENTAL HISTORY ====="
                );

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    System.out.println("--------------------------------");

                    System.out.println("Customer: " +
                            resultSet.getString("name"));

                    System.out.println("Vehicle: " +
                            resultSet.getString("brand") +
                            " " +
                            resultSet.getString("model"));

                    System.out.println("Vehicle Number: " +
                            resultSet.getString("vehicle_number"));

                    System.out.println("Rental Date: " +
                            resultSet.getDate("rental_date"));

                    System.out.println("Expected Return: " +
                            resultSet.getDate("expected_return_date"));

                    System.out.println("Actual Return: " +
                            resultSet.getDate("actual_return_date"));

                    System.out.println("Status: " +
                            resultSet.getString("status"));

                    System.out.println("Amount: ₹" +
                            resultSet.getDouble("total_amount"));
                }

                if (!found) {
                    System.out.println(
                            "No rental history found."
                    );
                }
            }
        }
    }
    // =========================================================
// TOTAL REVENUE
// =========================================================

    public void showTotalRevenue()
            throws SQLException {

        String sql = """
            SELECT SUM(total_amount) AS total_revenue
            FROM rentals
            WHERE status = 'COMPLETED'
            """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                double totalRevenue =
                        resultSet.getDouble("total_revenue");

                System.out.println(
                        "Total Revenue: ₹" +
                                totalRevenue
                );
            }
        }
    }
    // =========================================================
// REVENUE BY PAYMENT METHOD
// =========================================================

    public void showRevenueByPaymentMethod()
            throws SQLException {

        String sql = """
            SELECT
                payment_method,
                SUM(amount) AS total_revenue
            FROM payments
            WHERE payment_status = 'PAID'
            GROUP BY payment_method
            ORDER BY total_revenue DESC
            """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println(
                    "\n===== REVENUE BY PAYMENT METHOD ====="
            );

            while (resultSet.next()) {

                String paymentMethod =
                        resultSet.getString(
                                "payment_method"
                        );

                double totalRevenue =
                        resultSet.getDouble(
                                "total_revenue"
                        );

                System.out.println(
                        paymentMethod +
                                " : ₹" +
                                totalRevenue
                );
            }
        }
    }


// =========================================================
// MOST RENTED VEHICLES
// =========================================================

    public void showMostRentedVehicles()
            throws SQLException {

        String sql = """
            SELECT
                v.vehicle_id,
                v.vehicle_number,
                v.brand,
                v.model,
                COUNT(r.rental_id) AS rental_count
            FROM vehicles v
            JOIN rentals r
                ON v.vehicle_id = r.vehicle_id
            GROUP BY
                v.vehicle_id,
                v.vehicle_number,
                v.brand,
                v.model
            ORDER BY rental_count DESC
            """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println(
                    "\n===== MOST RENTED VEHICLES ====="
            );

            while (resultSet.next()) {

                int vehicleId =
                        resultSet.getInt(
                                "vehicle_id"
                        );

                String vehicleNumber =
                        resultSet.getString(
                                "vehicle_number"
                        );

                String brand =
                        resultSet.getString(
                                "brand"
                        );

                String model =
                        resultSet.getString(
                                "model"
                        );

                int rentalCount =
                        resultSet.getInt(
                                "rental_count"
                        );

                System.out.println(
                        "Vehicle ID: " +
                                vehicleId
                );

                System.out.println(
                        "Vehicle: " +
                                vehicleNumber +
                                " - " +
                                brand +
                                " " +
                                model
                );

                System.out.println(
                        "Rental Count: " +
                                rentalCount
                );

                System.out.println(
                        "-----------------------------"
                );
            }
        }
    }


// =========================================================
// DASHBOARD SUMMARY
// =========================================================

    public void showDashboardSummary()
            throws SQLException {

        String sql = """
            SELECT

                (SELECT COUNT(*)
                 FROM vehicles)
                    AS total_vehicles,

                (SELECT COUNT(*)
                 FROM vehicles
                 WHERE status = 'AVAILABLE')
                    AS available_vehicles,

                (SELECT COUNT(*)
                 FROM vehicles
                 WHERE status = 'RENTED')
                    AS rented_vehicles,

                (SELECT COUNT(*)
                 FROM customers)
                    AS total_customers,

                (SELECT COUNT(*)
                 FROM rentals
                 WHERE status = 'ACTIVE')
                    AS active_rentals,

                (SELECT COUNT(*)
                 FROM rentals
                 WHERE status = 'COMPLETED')
                    AS completed_rentals,

                (SELECT COALESCE(
                    SUM(amount), 0)
                 FROM payments
                 WHERE payment_status = 'PAID')
                    AS total_revenue
            """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                int totalVehicles =
                        resultSet.getInt(
                                "total_vehicles"
                        );

                int availableVehicles =
                        resultSet.getInt(
                                "available_vehicles"
                        );

                int rentedVehicles =
                        resultSet.getInt(
                                "rented_vehicles"
                        );

                int totalCustomers =
                        resultSet.getInt(
                                "total_customers"
                        );

                int activeRentals =
                        resultSet.getInt(
                                "active_rentals"
                        );

                int completedRentals =
                        resultSet.getInt(
                                "completed_rentals"
                        );

                double totalRevenue =
                        resultSet.getDouble(
                                "total_revenue"
                        );


                System.out.println(
                        "\n===== DASHBOARD SUMMARY ====="
                );

                System.out.println(
                        "Total Vehicles: " +
                                totalVehicles
                );

                System.out.println(
                        "Available Vehicles: " +
                                availableVehicles
                );

                System.out.println(
                        "Rented Vehicles: " +
                                rentedVehicles
                );

                System.out.println(
                        "Total Customers: " +
                                totalCustomers
                );

                System.out.println(
                        "Active Rentals: " +
                                activeRentals
                );

                System.out.println(
                        "Completed Rentals: " +
                                completedRentals
                );

                System.out.println(
                        "Total Revenue: ₹" +
                                totalRevenue
                );
            }
        }
    }
}