package com.vehiclerental.model;

public class Vehicle {

    private int vehicleId;
    private String vehicleNumber;
    private String brand;
    private String model;
    private String vehicleType;
    private double rentalRate;
    private String status;

    public Vehicle(String vehicleNumber,
                   String brand,
                   String model,
                   String vehicleType,
                   double rentalRate) {

        this.vehicleNumber = vehicleNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        setRentalRate(rentalRate);
        this.status = "AVAILABLE";
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {

        if (rentalRate < 0) {
            throw new IllegalArgumentException(
                    "Rental rate cannot be negative."
            );
        }

        this.rentalRate = rentalRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayInfo() {

        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Vehicle Type: " + vehicleType);
        System.out.println("Rental Rate: " + rentalRate);
        System.out.println("Status: " + status);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", rentalRate=" + rentalRate +
                ", status='" + status + '\'' +
                '}';
    }
}