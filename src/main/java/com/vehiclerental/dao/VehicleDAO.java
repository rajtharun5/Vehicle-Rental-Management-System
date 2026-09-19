package com.vehiclerental.dao;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // =========================================================
    // ADD VEHICLE
    // =========================================================

    public void addVehicle(Vehicle vehicle)
            throws SQLException {

        String sql = """
                INSERT INTO vehicles
                (vehicle_number,
                 brand,
                 model,
                 vehicle_type,
                 rental_rate,
                 status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    vehicle.getVehicleNumber()
            );

            statement.setString(
                    2,
                    vehicle.getBrand()
            );

            statement.setString(
                    3,
                    vehicle.getModel()
            );

            statement.setString(
                    4,
                    vehicle.getVehicleType()
            );

            statement.setDouble(
                    5,
                    vehicle.getRentalRate()
            );

            statement.setString(
                    6,
                    vehicle.getStatus()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    vehicle.setVehicleId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }


    // =========================================================
    // GET VEHICLE BY ID
    // =========================================================

    public Vehicle getVehicleById(int vehicleId)
            throws SQLException {

        String sql = """
                SELECT *
                FROM vehicles
                WHERE vehicle_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, vehicleId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapVehicle(resultSet);
                }
            }
        }

        return null;
    }


    // =========================================================
    // GET ALL VEHICLES
    // =========================================================

    public List<Vehicle> getAllVehicles()
            throws SQLException {

        String sql = """
                SELECT *
                FROM vehicles
                ORDER BY vehicle_id
                """;

        List<Vehicle> vehicles =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                vehicles.add(
                        mapVehicle(resultSet)
                );
            }
        }

        return vehicles;
    }


    // =========================================================
    // GET AVAILABLE VEHICLES
    // =========================================================

    public List<Vehicle> getAvailableVehicles()
            throws SQLException {

        String sql = """
                SELECT *
                FROM vehicles
                WHERE status = ?
                ORDER BY vehicle_id
                """;

        List<Vehicle> vehicles =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    "AVAILABLE"
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    vehicles.add(
                            mapVehicle(resultSet)
                    );
                }
            }
        }

        return vehicles;
    }


    // =========================================================
    // UPDATE VEHICLE
    // =========================================================

    public boolean updateVehicle(
            Vehicle vehicle)
            throws SQLException {

        String sql = """
                UPDATE vehicles
                SET vehicle_number = ?,
                    brand = ?,
                    model = ?,
                    vehicle_type = ?,
                    rental_rate = ?,
                    status = ?
                WHERE vehicle_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            setVehicleParameters(
                    statement,
                    vehicle
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // UPDATE VEHICLE - TRANSACTION VERSION
    // =========================================================

    public boolean updateVehicle(
            Vehicle vehicle,
            Connection connection)
            throws SQLException {

        String sql = """
                UPDATE vehicles
                SET vehicle_number = ?,
                    brand = ?,
                    model = ?,
                    vehicle_type = ?,
                    rental_rate = ?,
                    status = ?
                WHERE vehicle_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            setVehicleParameters(
                    statement,
                    vehicle
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // DELETE VEHICLE
    // =========================================================

    public boolean deleteVehicle(int vehicleId)
            throws SQLException {

        String sql = """
                DELETE FROM vehicles
                WHERE vehicle_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    vehicleId
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


    // =========================================================
    // SET VEHICLE PARAMETERS
    // =========================================================

    private void setVehicleParameters(
            PreparedStatement statement,
            Vehicle vehicle)
            throws SQLException {

        statement.setString(
                1,
                vehicle.getVehicleNumber()
        );

        statement.setString(
                2,
                vehicle.getBrand()
        );

        statement.setString(
                3,
                vehicle.getModel()
        );

        statement.setString(
                4,
                vehicle.getVehicleType()
        );

        statement.setDouble(
                5,
                vehicle.getRentalRate()
        );

        statement.setString(
                6,
                vehicle.getStatus()
        );

        statement.setInt(
                7,
                vehicle.getVehicleId()
        );
    }


    // =========================================================
    // MAP RESULT SET TO VEHICLE
    // =========================================================

    private Vehicle mapVehicle(
            ResultSet resultSet)
            throws SQLException {

        Vehicle vehicle =
                new Vehicle(
                        resultSet.getString(
                                "vehicle_number"
                        ),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getString("vehicle_type"),
                        resultSet.getDouble("rental_rate")
                );

        vehicle.setVehicleId(
                resultSet.getInt("vehicle_id")
        );

        vehicle.setStatus(
                resultSet.getString("status")
        );

        return vehicle;
    }
}