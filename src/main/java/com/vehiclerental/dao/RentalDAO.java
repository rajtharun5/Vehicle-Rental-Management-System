package com.vehiclerental.dao;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    // =========================================================
    // CREATE RENTAL
    // =========================================================

    public void createRental(Rental rental)
            throws SQLException {

        String sql = """
                INSERT INTO rentals
                (customer_id,
                 vehicle_id,
                 rental_date,
                 expected_return_date,
                 total_amount,
                 status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    rental.getCustomer().getCustomerId()
            );

            statement.setInt(
                    2,
                    rental.getVehicle().getVehicleId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            rental.getRentalDate()
                    )
            );

            statement.setDate(
                    4,
                    Date.valueOf(
                            rental.getExpectedReturnDate()
                    )
            );

            statement.setDouble(
                    5,
                    rental.getTotalAmount()
            );

            statement.setString(
                    6,
                    rental.getStatus()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    rental.setRentalId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }


    // =========================================================
    // CREATE RENTAL - TRANSACTION VERSION
    // =========================================================

    public void createRental(
            Rental rental,
            Connection connection)
            throws SQLException {

        String sql = """
                INSERT INTO rentals
                (customer_id,
                 vehicle_id,
                 rental_date,
                 expected_return_date,
                 total_amount,
                 status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    rental.getCustomer().getCustomerId()
            );

            statement.setInt(
                    2,
                    rental.getVehicle().getVehicleId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            rental.getRentalDate()
                    )
            );

            statement.setDate(
                    4,
                    Date.valueOf(
                            rental.getExpectedReturnDate()
                    )
            );

            statement.setDouble(
                    5,
                    rental.getTotalAmount()
            );

            statement.setString(
                    6,
                    rental.getStatus()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    rental.setRentalId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }


    // =========================================================
    // GET RENTAL BY ID
    // =========================================================

    public Rental getRentalById(int rentalId)
            throws SQLException {

        String sql = """
                SELECT
                    r.rental_id,
                    r.rental_date,
                    r.expected_return_date,
                    r.actual_return_date,
                    r.total_amount,
                    r.status,

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

                FROM rentals r

                JOIN customers c
                    ON r.customer_id = c.customer_id

                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id

                WHERE r.rental_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, rentalId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapRental(resultSet);
                }
            }
        }

        return null;
    }


    // =========================================================
    // GET ALL RENTALS
    // =========================================================

    public List<Rental> getAllRentals()
            throws SQLException {

        String sql = """
                SELECT
                    r.rental_id,
                    r.rental_date,
                    r.expected_return_date,
                    r.actual_return_date,
                    r.total_amount,
                    r.status,

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

                FROM rentals r

                JOIN customers c
                    ON r.customer_id = c.customer_id

                JOIN vehicles v
                    ON r.vehicle_id = v.vehicle_id

                ORDER BY r.rental_id
                """;

        List<Rental> rentals =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                rentals.add(
                        mapRental(resultSet)
                );
            }
        }

        return rentals;
    }


    // =========================================================
    // UPDATE RENTAL
    // =========================================================

    public boolean updateRental(
            Rental rental)
            throws SQLException {

        String sql = """
                UPDATE rentals
                SET expected_return_date = ?,
                    actual_return_date = ?,
                    total_amount = ?,
                    status = ?
                WHERE rental_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    Date.valueOf(
                            rental.getExpectedReturnDate()
                    )
            );

            if (rental.getActualReturnDate() != null) {

                statement.setDate(
                        2,
                        Date.valueOf(
                                rental.getActualReturnDate()
                        )
                );

            } else {

                statement.setNull(
                        2,
                        java.sql.Types.DATE
                );
            }

            statement.setDouble(
                    3,
                    rental.getTotalAmount()
            );

            statement.setString(
                    4,
                    rental.getStatus()
            );

            statement.setInt(
                    5,
                    rental.getRentalId()
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // RETURN VEHICLE
    // =========================================================

    public boolean returnVehicle(
            int rentalId,
            Date actualReturnDate,
            double totalAmount)
            throws SQLException {

        String sql = """
                UPDATE rentals
                SET actual_return_date = ?,
                    total_amount = ?,
                    status = 'COMPLETED'
                WHERE rental_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    actualReturnDate
            );

            statement.setDouble(
                    2,
                    totalAmount
            );

            statement.setInt(
                    3,
                    rentalId
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // RETURN VEHICLE - TRANSACTION VERSION
    // =========================================================

    public boolean returnVehicle(
            int rentalId,
            Date actualReturnDate,
            double totalAmount,
            Connection connection)
            throws SQLException {

        String sql = """
                UPDATE rentals
                SET actual_return_date = ?,
                    total_amount = ?,
                    status = 'COMPLETED'
                WHERE rental_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    actualReturnDate
            );

            statement.setDouble(
                    2,
                    totalAmount
            );

            statement.setInt(
                    3,
                    rentalId
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // MAP RESULT SET TO RENTAL OBJECT
    // =========================================================

    private Rental mapRental(
            ResultSet resultSet)
            throws SQLException {

        // ---------------------------------------------
        // Customer
        // ---------------------------------------------

        Customer customer =
                new Customer(
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString(
                                "license_number"
                        )
                );

        customer.setCustomerId(
                resultSet.getInt("customer_id")
        );


        // ---------------------------------------------
        // Vehicle
        // ---------------------------------------------

        Vehicle vehicle =
                new Vehicle(
                        resultSet.getString(
                                "vehicle_number"
                        ),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getString(
                                "vehicle_type"
                        ),
                        resultSet.getDouble(
                                "rental_rate"
                        )
                );

        vehicle.setVehicleId(
                resultSet.getInt("vehicle_id")
        );

        vehicle.setStatus(
                resultSet.getString(
                        "vehicle_status"
                )
        );


        // ---------------------------------------------
        // Rental
        // ---------------------------------------------

        Rental rental =
                new Rental(
                        customer,
                        vehicle,
                        resultSet.getDate(
                                "rental_date"
                        ).toLocalDate(),
                        resultSet.getDate(
                                "expected_return_date"
                        ).toLocalDate()
                );

        rental.setRentalId(
                resultSet.getInt("rental_id")
        );


        // ---------------------------------------------
        // Actual Return Date
        // ---------------------------------------------

        Date actualReturnDate =
                resultSet.getDate(
                        "actual_return_date"
                );

        if (actualReturnDate != null) {

            rental.setActualReturnDate(
                    actualReturnDate.toLocalDate()
            );
        }


        // ---------------------------------------------
        // Total Amount
        // ---------------------------------------------

        rental.setTotalAmount(
                resultSet.getDouble(
                        "total_amount"
                )
        );


        // ---------------------------------------------
        // Status
        // ---------------------------------------------

        rental.setStatus(
                resultSet.getString("status")
        );

        return rental;
    }
}