package com.vehiclerental.dao;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // CREATE
    public void addPayment(Payment payment) throws SQLException {

        String sql = """
                INSERT INTO payments
                (rental_id, amount, payment_method,
                 payment_date, payment_status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    payment.getRental().getRentalId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );

            statement.setString(
                    3,
                    payment.getPaymentMethod()
            );

            statement.setDate(
                    4,
                    Date.valueOf(payment.getPaymentDate())
            );

            statement.setString(
                    5,
                    payment.getPaymentStatus()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    payment.setPaymentId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }


    // READ - Get payment by ID
    public Payment getPaymentById(int paymentId) throws SQLException {

        String sql = """
                SELECT
                    p.payment_id,
                    p.amount,
                    p.payment_method,
                    p.payment_date,
                    p.payment_status,

                    r.rental_id,
                    r.rental_date,
                    r.expected_return_date,
                    r.actual_return_date,
                    r.total_amount,
                    r.status AS rental_status,

                    c.customer_id,
                    c.name,
                    c.phone,
                    c.email,
                    c.license_number,

                    v.vehicle_id,
                    v.vehicle_number,
                    v.brand,
                    v.model,
                    v.vehicle_type,
                    v.rental_rate,
                    v.status AS vehicle_status

                FROM payments p

                JOIN rentals r
                    ON p.rental_id = r.rental_id

                JOIN customers c
                    ON r.customer_id = c.customer_id

                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id

                WHERE p.payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

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


                    Vehicle vehicle = new Vehicle(
                            resultSet.getString("vehicle_number"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getString("vehicle_type"),
                            resultSet.getDouble("rental_rate")
                    );

                    vehicle.setVehicleId(
                            resultSet.getInt("vehicle_id")
                    );

                    vehicle.setStatus(
                            resultSet.getString("vehicle_status")
                    );


                    Rental rental = new Rental(
                            customer,
                            vehicle,
                            resultSet.getDate("rental_date").toLocalDate(),
                            resultSet.getDate("expected_return_date").toLocalDate()
                    );

                    rental.setRentalId(
                            resultSet.getInt("rental_id")
                    );

                    Date actualReturnDate =
                            resultSet.getDate("actual_return_date");

                    if (actualReturnDate != null) {

                        rental.setActualReturnDate(
                                actualReturnDate.toLocalDate()
                        );
                    }

                    double totalAmount =
                            resultSet.getDouble("total_amount");

                    if (!resultSet.wasNull()) {
                        rental.setTotalAmount(totalAmount);
                    }

                    rental.setStatus(
                            resultSet.getString("rental_status")
                    );


                    Payment payment = new Payment(
                            rental,
                            resultSet.getBigDecimal("amount"),
                            resultSet.getString("payment_method")
                    );

                    payment.setPaymentId(
                            resultSet.getInt("payment_id")
                    );

                    payment.setPaymentDate(
                            resultSet.getDate("payment_date")
                                    .toLocalDate()
                    );

                    payment.setPaymentStatus(
                            resultSet.getString("payment_status")
                    );

                    return payment;
                }
            }
        }

        return null;
    }


    // READ - Get all payments
    public List<Payment> getAllPayments() throws SQLException {

        String sql = """
                SELECT
                    p.payment_id,
                    p.amount,
                    p.payment_method,
                    p.payment_date,
                    p.payment_status,

                    r.rental_id,
                    r.rental_date,
                    r.expected_return_date,
                    r.actual_return_date,
                    r.total_amount,
                    r.status AS rental_status,

                    c.customer_id,
                    c.name,
                    c.phone,
                    c.email,
                    c.license_number,

                    v.vehicle_id,
                    v.vehicle_number,
                    v.brand,
                    v.model,
                    v.vehicle_type,
                    v.rental_rate,
                    v.status AS vehicle_status

                FROM payments p

                JOIN rentals r
                    ON p.rental_id = r.rental_id

                JOIN customers c
                    ON r.customer_id = c.customer_id

                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id

                ORDER BY p.payment_id
                """;

        List<Payment> payments = new ArrayList<>();

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


                Vehicle vehicle = new Vehicle(
                        resultSet.getString("vehicle_number"),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getString("vehicle_type"),
                        resultSet.getDouble("rental_rate")
                );

                vehicle.setVehicleId(
                        resultSet.getInt("vehicle_id")
                );

                vehicle.setStatus(
                        resultSet.getString("vehicle_status")
                );


                Rental rental = new Rental(
                        customer,
                        vehicle,
                        resultSet.getDate("rental_date").toLocalDate(),
                        resultSet.getDate("expected_return_date").toLocalDate()
                );

                rental.setRentalId(
                        resultSet.getInt("rental_id")
                );

                Date actualReturnDate =
                        resultSet.getDate("actual_return_date");

                if (actualReturnDate != null) {

                    rental.setActualReturnDate(
                            actualReturnDate.toLocalDate()
                    );
                }

                double totalAmount =
                        resultSet.getDouble("total_amount");

                if (!resultSet.wasNull()) {
                    rental.setTotalAmount(totalAmount);
                }

                rental.setStatus(
                        resultSet.getString("rental_status")
                );


                Payment payment = new Payment(
                        rental,
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("payment_method")
                );

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setPaymentDate(
                        resultSet.getDate("payment_date")
                                .toLocalDate()
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                payments.add(payment);
            }
        }

        return payments;
    }


    // UPDATE
    public boolean updatePayment(Payment payment)
            throws SQLException {

        String sql = """
                UPDATE payments
                SET amount = ?,
                    payment_method = ?,
                    payment_date = ?,
                    payment_status = ?
                WHERE payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(
                    1,
                    payment.getAmount()
            );

            statement.setString(
                    2,
                    payment.getPaymentMethod()
            );

            statement.setDate(
                    3,
                    Date.valueOf(payment.getPaymentDate())
            );

            statement.setString(
                    4,
                    payment.getPaymentStatus()
            );

            statement.setInt(
                    5,
                    payment.getPaymentId()
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // DELETE
    public boolean deletePayment(int paymentId)
            throws SQLException {

        String sql = """
                DELETE FROM payments
                WHERE payment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}