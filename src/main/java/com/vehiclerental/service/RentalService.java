package com.vehiclerental.service;

import com.vehiclerental.dao.CustomerDAO;
import com.vehiclerental.dao.RentalDAO;
import com.vehiclerental.dao.VehicleDAO;
import com.vehiclerental.exception.CustomerNotFoundException;
import com.vehiclerental.exception.RentalNotFoundException;
import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.exception.VehicleNotFoundException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.ValidationUtil;
import com.vehiclerental.util.DBConnection;

import java.sql.Connection;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {

    private final RentalDAO rentalDAO;
    private final CustomerDAO customerDAO;
    private final VehicleDAO vehicleDAO;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
        this.customerDAO = new CustomerDAO();
        this.vehicleDAO = new VehicleDAO();
    }


    // =========================================================
    // CREATE RENTAL
    // =========================================================

    public Rental createRental(
            int customerId,
            int vehicleId,
            LocalDate rentalDate,
            LocalDate expectedReturnDate)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                customerId,
                "Customer ID"
        );

        ValidationUtil.requirePositiveId(
                vehicleId,
                "Vehicle ID"
        );

        if (rentalDate == null) {
            throw new IllegalArgumentException(
                    "Rental date is required."
            );
        }

        if (expectedReturnDate == null) {
            throw new IllegalArgumentException(
                    "Expected return date is required."
            );
        }

        if (expectedReturnDate.isBefore(rentalDate)) {
            throw new IllegalArgumentException(
                    "Expected return date cannot be before rental date."
            );
        }


        // Find customer
        Customer customer =
                customerDAO.getCustomerById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer with ID " +
                            customerId +
                            " was not found."
            );
        }


        // Find vehicle
        Vehicle vehicle =
                vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    "Vehicle with ID " +
                            vehicleId +
                            " was not found."
            );
        }


        // Check availability
        if (!"AVAILABLE".equalsIgnoreCase(
                vehicle.getStatus())) {

            throw new VehicleNotAvailableException(
                    "Vehicle " +
                            vehicle.getVehicleNumber() +
                            " is currently not available."
            );
        }


        // Calculate rental days
        long days =
                ChronoUnit.DAYS.between(
                        rentalDate,
                        expectedReturnDate
                );

        if (days <= 0) {
            days = 1;
        }


        // Calculate total
        double totalAmount =
                days * vehicle.getRentalRate();


        // Create Rental object
        Rental rental =
                new Rental(
                        customer,
                        vehicle,
                        rentalDate,
                        expectedReturnDate
                );

        rental.setTotalAmount(totalAmount);


        // =========================================================
        // TRANSACTION
        // =========================================================

        try (Connection connection =
                     DBConnection.getConnection()) {

            try {

                // Start transaction
                connection.setAutoCommit(false);


                // Step 1: Create rental
                rentalDAO.createRental(
                        rental,
                        connection
                );


                // Step 2: Mark vehicle as RENTED
                vehicle.setStatus("RENTED");

                boolean updated =
                        vehicleDAO.updateVehicle(
                                vehicle,
                                connection
                        );


                if (!updated) {

                    throw new SQLException(
                            "Vehicle status could not be updated."
                    );
                }


                // Both operations succeeded
                connection.commit();

                return rental;

            } catch (SQLException e) {

                // Something failed
                connection.rollback();

                throw e;
            }
        }
    }

    // =========================================================
    // GET RENTAL
    // =========================================================

    public Rental getRentalById(int rentalId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                rentalId,
                "Rental ID"
        );

        Rental rental =
                rentalDAO.getRentalById(rentalId);

        if (rental == null) {
            throw new RentalNotFoundException(
                    "Rental with ID " +
                            rentalId +
                            " was not found."
            );
        }

        return rental;
    }


    // =========================================================
    // GET ALL RENTALS
    // =========================================================

    public List<Rental> getAllRentals()
            throws SQLException {

        return rentalDAO.getAllRentals();
    }


    // =========================================================
    // RETURN VEHICLE
    // =========================================================

    public boolean returnVehicle(
            int rentalId,
            LocalDate actualReturnDate)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                rentalId,
                "Rental ID"
        );

        if (actualReturnDate == null) {
            throw new IllegalArgumentException(
                    "Actual return date is required."
            );
        }


        // Find rental
        Rental rental =
                rentalDAO.getRentalById(rentalId);

        if (rental == null) {
            throw new RentalNotFoundException(
                    "Rental with ID " +
                            rentalId +
                            " was not found."
            );
        }


        // Check status
        if (!"ACTIVE".equalsIgnoreCase(
                rental.getStatus())) {

            throw new IllegalArgumentException(
                    "Rental is already completed."
            );
        }


        // Validate date
        if (actualReturnDate.isBefore(
                rental.getRentalDate())) {

            throw new IllegalArgumentException(
                    "Return date cannot be before rental date."
            );
        }


        // Calculate days
        long days =
                ChronoUnit.DAYS.between(
                        rental.getRentalDate(),
                        actualReturnDate
                );

        if (days <= 0) {
            days = 1;
        }


        // Calculate final amount
        double totalAmount =
                days *
                        rental.getVehicle().getRentalRate();


        // Update rental
        boolean updated =
                rentalDAO.returnVehicle(
                        rentalId,
                        Date.valueOf(actualReturnDate),
                        totalAmount
                );

        if (!updated) {
            throw new RentalNotFoundException(
                    "Rental could not be updated."
            );
        }


        // Make vehicle available
        Vehicle vehicle =
                rental.getVehicle();

        vehicle.setStatus("AVAILABLE");

        vehicleDAO.updateVehicle(vehicle);

        return true;
    }
}