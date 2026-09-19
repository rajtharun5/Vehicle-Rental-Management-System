package com.vehiclerental.main;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.CustomerService;
import com.vehiclerental.service.PaymentService;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.service.ReportService;
import com.vehiclerental.service.VehicleService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    // =========================================================
    // SERVICES & SCANNER
    // =========================================================

    private static final Scanner scanner = new Scanner(System.in);

    private static final VehicleService vehicleService =
            new VehicleService();

    private static final CustomerService customerService =
            new CustomerService();

    private static final RentalService rentalService =
            new RentalService();

    private static final PaymentService paymentService =
            new PaymentService();

    private static final ReportService reportService =
            new ReportService();


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        while (true) {

            displayMainMenu();

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1 -> vehicleMenu();

                    case 2 -> customerMenu();

                    case 3 -> rentalMenu();

                    case 4 -> paymentMenu();

                    case 5 -> reportsMenu();

                    case 6 -> {

                        System.out.println();
                        System.out.println(
                                "Thank you for using " +
                                        "Vehicle Rental Management System."
                        );

                        scanner.close();
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice. Please try again."
                            );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Validation Error: " +
                                e.getMessage()
                );

            } catch (SQLException e) {

                System.out.println(
                        "Database Error: " +
                                e.getMessage()
                );

            } catch (Exception e) {

                System.out.println(
                        "Unexpected Error: " +
                                e.getMessage()
                );
            }
        }
    }


    // =========================================================
    // MAIN MENU
    // =========================================================

    private static void displayMainMenu() {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "      VEHICLE RENTAL MANAGEMENT SYSTEM"
        );
        System.out.println(
                "=============================================="
        );

        System.out.println("1. Vehicle Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Rental Management");
        System.out.println("4. Payment Management");
        System.out.println("5. Reports");
        System.out.println("6. Exit");

        System.out.println(
                "=============================================="
        );
    }


    // =========================================================
    // VEHICLE MENU
    // =========================================================

    private static void vehicleMenu()
            throws SQLException {

        while (true) {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "             VEHICLE MANAGEMENT"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println("1. Add Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. View Vehicle By ID");
            System.out.println("4. View Available Vehicles");
            System.out.println("5. Update Vehicle");
            System.out.println("6. Delete Vehicle");
            System.out.println("7. Back to Main Menu");

            System.out.println(
                    "=============================================="
            );

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1 -> addVehicle();

                case 2 -> viewAllVehicles();

                case 3 -> viewVehicleById();

                case 4 -> viewAvailableVehicles();

                case 5 -> updateVehicle();

                case 6 -> deleteVehicle();

                case 7 -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }


    // =========================================================
    // ADD VEHICLE
    // =========================================================

    private static void addVehicle()
            throws SQLException {

        System.out.println();
        System.out.println("----- Add Vehicle -----");

        String vehicleNumber =
                readText("Vehicle Number: ");

        String brand =
                readText("Brand: ");

        String model =
                readText("Model: ");

        String vehicleType =
                readText("Vehicle Type: ");

        double rentalRate =
                readDouble("Rental Rate Per Day: ");

        Vehicle vehicle =
                new Vehicle(
                        vehicleNumber,
                        brand,
                        model,
                        vehicleType,
                        rentalRate
                );

        vehicleService.addVehicle(vehicle);

        System.out.println();
        System.out.println(
                "Vehicle added successfully."
        );

        System.out.println(
                "Vehicle ID: " +
                        vehicle.getVehicleId()
        );
    }


    // =========================================================
    // VIEW ALL VEHICLES
    // =========================================================

    private static void viewAllVehicles()
            throws SQLException {

        System.out.println();
        System.out.println("----- All Vehicles -----");

        List<Vehicle> vehicles =
                vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {

            System.out.println(
                    "No vehicles found."
            );

            return;
        }

        for (Vehicle vehicle : vehicles) {

            displayVehicle(vehicle);
        }

        System.out.println(
                "----------------------------------------------"
        );
    }


    // =========================================================
    // VIEW VEHICLE BY ID
    // =========================================================

    private static void viewVehicleById()
            throws SQLException {

        int vehicleId =
                readInt("Enter Vehicle ID: ");

        Vehicle vehicle =
                vehicleService.getVehicleById(
                        vehicleId
                );

        System.out.println();

        displayVehicle(vehicle);
    }


    // =========================================================
    // VIEW AVAILABLE VEHICLES
    // =========================================================

    private static void viewAvailableVehicles()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Available Vehicles -----"
        );

        List<Vehicle> vehicles =
                vehicleService.getAvailableVehicles();

        if (vehicles.isEmpty()) {

            System.out.println(
                    "No vehicles are currently available."
            );

            return;
        }

        for (Vehicle vehicle : vehicles) {

            displayVehicle(vehicle);
        }

        System.out.println(
                "----------------------------------------------"
        );
    }


    // =========================================================
    // UPDATE VEHICLE
    // =========================================================

    private static void updateVehicle() {

        try {

            int vehicleId =
                    readInt("Enter Vehicle ID: ");

            Vehicle vehicle =
                    vehicleService.getVehicleById(
                            vehicleId
                    );

            System.out.println();
            System.out.println(
                    "Current Vehicle Details:"
            );

            displayVehicle(vehicle);

            while (true) {

                System.out.println();
                System.out.println(
                        "===== UPDATE VEHICLE ====="
                );

                System.out.println(
                        "1. Vehicle Number"
                );

                System.out.println(
                        "2. Brand"
                );

                System.out.println(
                        "3. Model"
                );

                System.out.println(
                        "4. Vehicle Type"
                );

                System.out.println(
                        "5. Rental Rate"
                );

                System.out.println(
                        "6. Status"
                );

                System.out.println(
                        "7. Cancel"
                );

                int choice =
                        readInt(
                                "What do you want to update? "
                        );

                if (choice == 7) {

                    System.out.println(
                            "Update cancelled."
                    );

                    return;
                }

                String newValue;

                switch (choice) {

                    case 1 ->
                            newValue =
                                    readText(
                                            "Enter new Vehicle Number: "
                                    );

                    case 2 ->
                            newValue =
                                    readText(
                                            "Enter new Brand: "
                                    );

                    case 3 ->
                            newValue =
                                    readText(
                                            "Enter new Model: "
                                    );

                    case 4 ->
                            newValue =
                                    readText(
                                            "Enter new Vehicle Type: "
                                    );

                    case 5 ->
                            newValue =
                                    readText(
                                            "Enter new Rental Rate: "
                                    );

                    case 6 ->
                            newValue =
                                    readText(
                                            "Enter new Status (AVAILABLE/RENTED): "
                                    );

                    default -> {

                        System.out.println(
                                "Invalid choice."
                        );

                        continue;
                    }
                }

                boolean updated =
                        vehicleService.updateVehicleField(
                                vehicleId,
                                choice,
                                newValue
                        );

                if (updated) {

                    System.out.println();
                    System.out.println(
                            "Vehicle updated successfully."
                    );

                    break;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error: " +
                            e.getMessage()
            );
        }
    }


    // =========================================================
    // DELETE VEHICLE
    // =========================================================

    private static void deleteVehicle()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Delete Vehicle -----"
        );

        int vehicleId =
                readInt("Enter Vehicle ID: ");

        Vehicle vehicle =
                vehicleService.getVehicleById(
                        vehicleId
                );

        System.out.println(
                "Vehicle: " +
                        vehicle.getVehicleNumber() +
                        " - " +
                        vehicle.getBrand() +
                        " " +
                        vehicle.getModel()
        );

        String confirmation =
                readText(
                        "Are you sure you want to delete this vehicle? (yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Delete operation cancelled."
            );

            return;
        }

        boolean deleted =
                vehicleService.deleteVehicle(
                        vehicleId
                );

        if (deleted) {

            System.out.println();
            System.out.println(
                    "Vehicle deleted successfully."
            );
        }
    }


    // =========================================================
    // CUSTOMER MENU
    // =========================================================

    private static void customerMenu()
            throws SQLException {

        while (true) {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "             CUSTOMER MANAGEMENT"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. View Customer By ID");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Back to Main Menu");

            System.out.println(
                    "=============================================="
            );

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1 -> addCustomer();

                case 2 -> viewAllCustomers();

                case 3 -> viewCustomerById();

                case 4 -> updateCustomer();

                case 5 -> deleteCustomer();

                case 6 -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }


    // =========================================================
    // ADD CUSTOMER
    // =========================================================

    private static void addCustomer()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Add Customer -----"
        );

        String name =
                readText("Name: ");

        String phone =
                readText("Phone: ");

        String email =
                readText("Email: ");

        String licenseNumber =
                readText("License Number: ");

        Customer customer =
                new Customer(
                        name,
                        phone,
                        email,
                        licenseNumber
                );

        customerService.addCustomer(customer);

        System.out.println();
        System.out.println(
                "Customer added successfully."
        );

        System.out.println(
                "Customer ID: " +
                        customer.getCustomerId()
        );
    }


    // =========================================================
    // VIEW ALL CUSTOMERS
    // =========================================================

    private static void viewAllCustomers()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- All Customers -----"
        );

        List<Customer> customers =
                customerService.getAllCustomers();

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers found."
            );

            return;
        }

        for (Customer customer : customers) {

            displayCustomer(customer);
        }

        System.out.println(
                "----------------------------------------------"
        );
    }


    // =========================================================
    // VIEW CUSTOMER BY ID
    // =========================================================

    private static void viewCustomerById()
            throws SQLException {

        int customerId =
                readInt("Enter Customer ID: ");

        Customer customer =
                customerService.getCustomerById(
                        customerId
                );

        System.out.println();

        displayCustomer(customer);
    }


    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    private static void updateCustomer()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Update Customer -----"
        );

        int customerId =
                readInt("Enter Customer ID: ");

        Customer customer =
                customerService.getCustomerById(
                        customerId
                );

        System.out.println(
                "Current Name: " +
                        customer.getName()
        );

        String name =
                readText("Enter New Name: ");

        String phone =
                readText("Enter New Phone: ");

        String email =
                readText("Enter New Email: ");

        String licenseNumber =
                readText(
                        "Enter New License Number: "
                );

        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.setLicenseNumber(licenseNumber);

        boolean updated =
                customerService.updateCustomer(
                        customer
                );

        if (updated) {

            System.out.println();
            System.out.println(
                    "Customer updated successfully."
            );
        }
    }


    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    private static void deleteCustomer()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Delete Customer -----"
        );

        int customerId =
                readInt("Enter Customer ID: ");

        Customer customer =
                customerService.getCustomerById(
                        customerId
                );

        System.out.println(
                "Customer: " +
                        customer.getName()
        );

        String confirmation =
                readText(
                        "Are you sure you want to delete this customer? (yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Delete operation cancelled."
            );

            return;
        }

        boolean deleted =
                customerService.deleteCustomer(
                        customerId
                );

        if (deleted) {

            System.out.println();
            System.out.println(
                    "Customer deleted successfully."
            );
        }
    }


    // =========================================================
    // RENTAL MENU
    // =========================================================

    private static void rentalMenu()
            throws SQLException {

        while (true) {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "              RENTAL MANAGEMENT"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println("1. Create Rental");
            System.out.println("2. View All Rentals");
            System.out.println("3. View Rental By ID");
            System.out.println("4. Return Vehicle");
            System.out.println("5. Back to Main Menu");

            System.out.println(
                    "=============================================="
            );

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1 -> createRental();

                case 2 -> viewAllRentals();

                case 3 -> viewRentalById();

                case 4 -> returnVehicle();

                case 5 -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }


    // =========================================================
    // CREATE RENTAL
    // =========================================================

    private static void createRental()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Create Rental -----"
        );

        int customerId =
                readInt("Enter Customer ID: ");

        int vehicleId =
                readInt("Enter Vehicle ID: ");

        LocalDate rentalDate =
                readDate(
                        "Enter Rental Date (YYYY-MM-DD): "
                );

        LocalDate expectedReturnDate =
                readDate(
                        "Enter Expected Return Date (YYYY-MM-DD): "
                );

        Rental rental =
                rentalService.createRental(
                        customerId,
                        vehicleId,
                        rentalDate,
                        expectedReturnDate
                );

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Rental created successfully!"
        );

        System.out.println(
                "Rental ID: " +
                        rental.getRentalId()
        );

        System.out.println(
                "Customer: " +
                        rental.getCustomer().getName()
        );

        System.out.println(
                "Vehicle: " +
                        rental.getVehicle().getVehicleNumber()
        );

        System.out.println(
                "Rental Date: " +
                        rental.getRentalDate()
        );

        System.out.println(
                "Expected Return Date: " +
                        rental.getExpectedReturnDate()
        );

        System.out.println(
                "Total Amount: ₹" +
                        rental.getTotalAmount()
        );

        System.out.println(
                "Status: " +
                        rental.getStatus()
        );

        System.out.println(
                "=============================================="
        );
    }


    // =========================================================
    // VIEW ALL RENTALS
    // =========================================================

    private static void viewAllRentals()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- All Rentals -----"
        );

        List<Rental> rentals =
                rentalService.getAllRentals();

        if (rentals.isEmpty()) {

            System.out.println(
                    "No rentals found."
            );

            return;
        }

        for (Rental rental : rentals) {

            displayRental(rental);
        }

        System.out.println(
                "----------------------------------------------"
        );
    }


    // =========================================================
    // VIEW RENTAL BY ID
    // =========================================================

    private static void viewRentalById()
            throws SQLException {

        int rentalId =
                readInt("Enter Rental ID: ");

        Rental rental =
                rentalService.getRentalById(
                        rentalId
                );

        System.out.println();

        displayRental(rental);
    }


    // =========================================================
    // RETURN VEHICLE
    // =========================================================

    private static void returnVehicle()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Return Vehicle -----"
        );

        int rentalId =
                readInt("Enter Rental ID: ");

        LocalDate actualReturnDate =
                readDate(
                        "Enter Actual Return Date (YYYY-MM-DD): "
                );

        boolean returned =
                rentalService.returnVehicle(
                        rentalId,
                        actualReturnDate
                );

        if (returned) {

            System.out.println();
            System.out.println(
                    "Vehicle returned successfully."
            );

            System.out.println(
                    "Rental has been marked as COMPLETED."
            );

            System.out.println(
                    "Vehicle is now AVAILABLE."
            );
        }
    }


    // =========================================================
    // PAYMENT MENU
    // =========================================================

    private static void paymentMenu()
            throws SQLException {

        while (true) {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "              PAYMENT MANAGEMENT"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println("1. Add Payment");
            System.out.println("2. View All Payments");
            System.out.println("3. View Payment By ID");
            System.out.println("4. Update Payment");
            System.out.println("5. Delete Payment");
            System.out.println("6. Back to Main Menu");

            System.out.println(
                    "=============================================="
            );

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1 -> addPayment();

                case 2 -> viewAllPayments();

                case 3 -> viewPaymentById();

                case 4 -> updatePayment();

                case 5 -> deletePayment();

                case 6 -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }


    // =========================================================
    // ADD PAYMENT
    // =========================================================

    private static void addPayment()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Add Payment -----"
        );

        int rentalId =
                readInt("Enter Rental ID: ");

        BigDecimal amount =
                readBigDecimal(
                        "Enter Payment Amount: ₹"
                );

        String paymentMethod =
                readText(
                        "Enter Payment Method: "
                );

        paymentService.addPayment(
                rentalId,
                amount,
                paymentMethod
        );

        System.out.println();
        System.out.println(
                "Payment added successfully."
        );
    }


    // =========================================================
    // VIEW ALL PAYMENTS
    // =========================================================

    private static void viewAllPayments()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- All Payments -----"
        );

        List<Payment> payments =
                paymentService.getAllPayments();

        if (payments.isEmpty()) {

            System.out.println(
                    "No payments found."
            );

            return;
        }

        for (Payment payment : payments) {

            displayPayment(payment);
        }

        System.out.println(
                "----------------------------------------------"
        );
    }


    // =========================================================
    // VIEW PAYMENT BY ID
    // =========================================================

    private static void viewPaymentById()
            throws SQLException {

        int paymentId =
                readInt("Enter Payment ID: ");

        Payment payment =
                paymentService.getPaymentById(
                        paymentId
                );

        System.out.println();

        displayPayment(payment);
    }


    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    private static void updatePayment()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Update Payment -----"
        );

        int paymentId =
                readInt("Enter Payment ID: ");

        Payment payment =
                paymentService.getPaymentById(
                        paymentId
                );

        System.out.println(
                "Current Amount: ₹" +
                        payment.getAmount()
        );

        System.out.println(
                "Current Method: " +
                        payment.getPaymentMethod()
        );

        BigDecimal amount =
                readBigDecimal(
                        "Enter New Amount: ₹"
                );

        String paymentMethod =
                readText(
                        "Enter New Payment Method: "
                );

        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);

        boolean updated =
                paymentService.updatePayment(
                        payment
                );

        if (updated) {

            System.out.println();
            System.out.println(
                    "Payment updated successfully."
            );
        }
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    private static void deletePayment()
            throws SQLException {

        System.out.println();
        System.out.println(
                "----- Delete Payment -----"
        );

        int paymentId =
                readInt("Enter Payment ID: ");

        Payment payment =
                paymentService.getPaymentById(
                        paymentId
                );

        System.out.println(
                "Payment Amount: ₹" +
                        payment.getAmount()
        );

        System.out.println(
                "Payment Method: " +
                        payment.getPaymentMethod()
        );

        String confirmation =
                readText(
                        "Are you sure you want to delete this payment? (yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Delete operation cancelled."
            );

            return;
        }

        boolean deleted =
                paymentService.deletePayment(
                        paymentId
                );

        if (deleted) {

            System.out.println();
            System.out.println(
                    "Payment deleted successfully."
            );
        }
    }


    // =========================================================
    // REPORTS MENU
    // =========================================================

    private static void reportsMenu()
            throws SQLException {

        while (true) {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "                 REPORTS"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "1. Available Vehicles"
            );

            System.out.println(
                    "2. Active Rentals"
            );

            System.out.println(
                    "3. Completed Rentals"
            );

            System.out.println(
                    "4. Customer Rental History"
            );

            System.out.println(
                    "5. Total Revenue"
            );

            System.out.println(
                    "6. Revenue By Payment Method"
            );

            System.out.println(
                    "7. Most Rented Vehicles"
            );

            System.out.println(
                    "8. Dashboard Summary"
            );

            System.out.println("9. Back");

            int choice =
                    readInt("Enter your choice: ");

            switch (choice) {

                case 1 ->
                        reportService.showAvailableVehicles();

                case 2 ->
                        reportService.showActiveRentals();

                case 3 ->
                        reportService.showCompletedRentals();

                case 4 -> {

                    int customerId =
                            readInt(
                                    "Enter Customer ID: "
                            );

                    reportService.showCustomerRentalHistory(
                            customerId
                    );
                }

                case 5 ->
                        reportService.showTotalRevenue();

                case 6 ->
                        reportService
                                .showRevenueByPaymentMethod();

                case 7 ->
                        reportService.showMostRentedVehicles();

                case 8 ->
                        reportService.showDashboardSummary();

                case 9 -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }


    // =========================================================
    // INPUT HELPERS
    // =========================================================

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine().trim();

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid integer."
                );
            }
        }
    }


    private static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine().trim();

                return Double.parseDouble(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }


    private static BigDecimal readBigDecimal(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine().trim();

                return new BigDecimal(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid amount."
                );
            }
        }
    }


    private static LocalDate readDate(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine().trim();

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please use YYYY-MM-DD."
                );
            }
        }
    }


    private static String readText(
            String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }


    // =========================================================
    // DISPLAY HELPERS
    // =========================================================

    private static void displayVehicle(
            Vehicle vehicle) {

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "ID: " +
                        vehicle.getVehicleId()
        );

        System.out.println(
                "Vehicle Number: " +
                        vehicle.getVehicleNumber()
        );

        System.out.println(
                "Brand: " +
                        vehicle.getBrand()
        );

        System.out.println(
                "Model: " +
                        vehicle.getModel()
        );

        System.out.println(
                "Type: " +
                        vehicle.getVehicleType()
        );

        System.out.println(
                "Rental Rate: ₹" +
                        vehicle.getRentalRate()
        );

        System.out.println(
                "Status: " +
                        vehicle.getStatus()
        );
    }


    private static void displayCustomer(
            Customer customer) {

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "ID: " +
                        customer.getCustomerId()
        );

        System.out.println(
                "Name: " +
                        customer.getName()
        );

        System.out.println(
                "Phone: " +
                        customer.getPhone()
        );

        System.out.println(
                "Email: " +
                        customer.getEmail()
        );

        System.out.println(
                "License Number: " +
                        customer.getLicenseNumber()
        );
    }


    private static void displayRental(
            Rental rental) {

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "Rental ID: " +
                        rental.getRentalId()
        );

        System.out.println(
                "Customer: " +
                        rental.getCustomer().getName()
        );

        System.out.println(
                "Vehicle: " +
                        rental.getVehicle()
                                .getVehicleNumber()
        );

        System.out.println(
                "Rental Date: " +
                        rental.getRentalDate()
        );

        System.out.println(
                "Expected Return: " +
                        rental.getExpectedReturnDate()
        );

        System.out.println(
                "Actual Return: " +
                        rental.getActualReturnDate()
        );

        System.out.println(
                "Total Amount: ₹" +
                        rental.getTotalAmount()
        );

        System.out.println(
                "Status: " +
                        rental.getStatus()
        );
    }


    private static void displayPayment(
            Payment payment) {

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "Payment ID: " +
                        payment.getPaymentId()
        );

        System.out.println(
                "Rental ID: " +
                        payment.getRental()
                                .getRentalId()
        );

        System.out.println(
                "Customer: " +
                        payment.getRental()
                                .getCustomer()
                                .getName()
        );

        System.out.println(
                "Vehicle: " +
                        payment.getRental()
                                .getVehicle()
                                .getVehicleNumber()
        );

        System.out.println(
                "Amount: ₹" +
                        payment.getAmount()
        );

        System.out.println(
                "Payment Method: " +
                        payment.getPaymentMethod()
        );

        System.out.println(
                "Payment Date: " +
                        payment.getPaymentDate()
        );

        System.out.println(
                "Payment Status: " +
                        payment.getPaymentStatus()
        );
    }
}