package com.vehiclerental.model;

import java.time.LocalDate;

public class Rental {

    private int rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate rentalDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double totalAmount;
    private String status;

    public Rental(Customer customer,
                  Vehicle vehicle,
                  LocalDate rentalDate,
                  LocalDate expectedReturnDate) {

        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDate = rentalDate;
        this.expectedReturnDate = expectedReturnDate;
        this.status = "ACTIVE";
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", customer=" + customer +
                ", vehicle=" + vehicle +
                ", rentalDate=" + rentalDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", actualReturnDate=" + actualReturnDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}