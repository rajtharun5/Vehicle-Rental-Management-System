package com.vehiclerental.service;

import com.vehiclerental.dao.VehicleDAO;
import com.vehiclerental.exception.VehicleNotFoundException;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO;

    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }


    // =========================================================
    // ADD VEHICLE
    // =========================================================

    public void addVehicle(Vehicle vehicle)
            throws SQLException {

        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Vehicle cannot be null."
            );
        }

        ValidationUtil.requireText(
                vehicle.getVehicleNumber(),
                "Vehicle number"
        );

        ValidationUtil.requireText(
                vehicle.getBrand(),
                "Vehicle brand"
        );

        ValidationUtil.requireText(
                vehicle.getModel(),
                "Vehicle model"
        );

        ValidationUtil.requireText(
                vehicle.getVehicleType(),
                "Vehicle type"
        );

        ValidationUtil.requirePositiveAmount(
                vehicle.getRentalRate(),
                "Rental rate"
        );

        vehicleDAO.addVehicle(vehicle);
    }


    // =========================================================
    // GET VEHICLE
    // =========================================================

    public Vehicle getVehicleById(int vehicleId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                vehicleId,
                "Vehicle ID"
        );

        Vehicle vehicle =
                vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    "Vehicle with ID " +
                            vehicleId +
                            " was not found."
            );
        }

        return vehicle;
    }


    // =========================================================
    // GET ALL VEHICLES
    // =========================================================

    public List<Vehicle> getAllVehicles()
            throws SQLException {

        return vehicleDAO.getAllVehicles();
    }


    // =========================================================
    // GET AVAILABLE VEHICLES
    // =========================================================

    public List<Vehicle> getAvailableVehicles()
            throws SQLException {

        return vehicleDAO.getAvailableVehicles();
    }


    // =========================================================
    // UPDATE VEHICLE
    // =========================================================

    public boolean updateVehicle(Vehicle vehicle)
            throws SQLException {

        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Vehicle cannot be null."
            );
        }

        ValidationUtil.requirePositiveId(
                vehicle.getVehicleId(),
                "Vehicle ID"
        );

        ValidationUtil.requireText(
                vehicle.getVehicleNumber(),
                "Vehicle number"
        );

        ValidationUtil.requireText(
                vehicle.getBrand(),
                "Vehicle brand"
        );

        ValidationUtil.requireText(
                vehicle.getModel(),
                "Vehicle model"
        );

        ValidationUtil.requirePositiveAmount(
                vehicle.getRentalRate(),
                "Rental rate"
        );

        boolean updated =
                vehicleDAO.updateVehicle(vehicle);

        if (!updated) {
            throw new VehicleNotFoundException(
                    "Vehicle with ID " +
                            vehicle.getVehicleId() +
                            " was not found."
            );
        }

        return true;
    }


    // =========================================================
    // DELETE VEHICLE
    // =========================================================

    public boolean deleteVehicle(int vehicleId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                vehicleId,
                "Vehicle ID"
        );

        boolean deleted =
                vehicleDAO.deleteVehicle(vehicleId);

        if (!deleted) {
            throw new VehicleNotFoundException(
                    "Vehicle with ID " +
                            vehicleId +
                            " was not found."
            );
        }

        return true;
    }
    // =========================================================
// UPDATE SINGLE VEHICLE FIELD
// =========================================================

    public boolean updateVehicleField(
            int vehicleId,
            int choice,
            String value)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                vehicleId,
                "Vehicle ID"
        );

        Vehicle vehicle =
                vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {

            throw new VehicleNotFoundException(
                    "Vehicle with ID " +
                            vehicleId +
                            " was not found."
            );
        }

        if (value == null || value.isBlank()) {

            throw new IllegalArgumentException(
                    "Value cannot be empty."
            );
        }

        switch (choice) {

            case 1:

                vehicle.setVehicleNumber(value);
                break;

            case 2:

                vehicle.setBrand(value);
                break;

            case 3:

                vehicle.setModel(value);
                break;

            case 4:

                vehicle.setVehicleType(value);
                break;

            case 5:

                double rentalRate;

                try {

                    rentalRate =
                            Double.parseDouble(value);

                } catch (NumberFormatException e) {

                    throw new IllegalArgumentException(
                            "Rental rate must be a valid number."
                    );
                }

                if (rentalRate <= 0) {

                    throw new IllegalArgumentException(
                            "Rental rate must be greater than zero."
                    );
                }

                vehicle.setRentalRate(rentalRate);
                break;

            case 6:

                if (!value.equalsIgnoreCase("AVAILABLE")
                        && !value.equalsIgnoreCase("RENTED")) {

                    throw new IllegalArgumentException(
                            "Status must be AVAILABLE or RENTED."
                    );
                }

                vehicle.setStatus(
                        value.toUpperCase()
                );

                break;

            default:

                throw new IllegalArgumentException(
                        "Invalid update choice."
                );
        }

        return vehicleDAO.updateVehicle(vehicle);
    }
}