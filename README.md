# Vehicle Rental Management System

A console-based **Vehicle Rental Management System** developed using **Java, OOP, JDBC, MySQL, and SQL**.

The system manages vehicles, customers, rentals, payments, and business reports through a layered architecture.

---

## Features

* Vehicle management

    * Add vehicle
    * View all vehicles
    * View vehicle by ID
    * View available vehicles
    * Update vehicle details
    * Delete vehicle

* Customer management

    * Add customer
    * View all customers
    * View customer by ID
    * Update customer
    * Delete customer

* Rental management

    * Create rental
    * View all rentals
    * View rental by ID
    * Return vehicle
    * Automatic rental amount calculation
    * Vehicle availability management

* Payment management

    * Add payment
    * View payments
    * View payment by ID
    * Update payment
    * Delete payment

* Reports

    * Available vehicles
    * Active rentals
    * Completed rentals
    * Customer rental history
    * Total revenue
    * Revenue by payment method
    * Most rented vehicles
    * Dashboard summary

* Validation and exception handling

* JDBC transaction management

* MySQL database integration

* Layered architecture

---

## Tech Stack

| Technology    | Usage                   |
| ------------- | ----------------------- |
| Java 21       | Application development |
| OOP           | Object-oriented design  |
| JDBC          | Database connectivity   |
| MySQL         | Database                |
| SQL           | Queries and reports     |
| Maven         | Dependency management   |
| Git           | Version control         |
| GitHub        | Source code hosting     |
| IntelliJ IDEA | Development environment |

---

## Architecture

The application follows a layered architecture:

```text
User
  ↓
Main / Console UI
  ↓
Service Layer
  ↓
DAO Layer
  ↓
JDBC
  ↓
MySQL Database
```

### Layers

**Main Layer**

Handles console menus, user input, and displaying results.

**Service Layer**

Contains business logic, validation, calculations, and transaction handling.

**DAO Layer**

Handles database operations using JDBC and SQL.

**Model Layer**

Contains Java classes representing:

* Vehicle
* Customer
* Rental
* Payment

**Utility Layer**

Contains database connection and validation utilities.

**Exception Layer**

Contains custom exceptions for application-specific errors.

---

## Project Structure

```text
VehicleRentalManagementSystem
│
├── database
│   └── schema.sql
│
├── src
│   └── main
│       └── java
│           └── com
│               └── vehiclerental
│                   ├── dao
│                   │   ├── CustomerDAO.java
│                   │   ├── PaymentDAO.java
│                   │   ├── RentalDAO.java
│                   │   ├── ReportDAO.java
│                   │   └── VehicleDAO.java
│                   │
│                   ├── exception
│                   │   ├── CustomerNotFoundException.java
│                   │   ├── InvalidPaymentException.java
│                   │   ├── PaymentNotFoundException.java
│                   │   ├── RentalNotFoundException.java
│                   │   ├── VehicleNotAvailableException.java
│                   │   └── VehicleNotFoundException.java
│                   │
│                   ├── main
│                   │   └── Main.java
│                   │
│                   ├── model
│                   │   ├── Customer.java
│                   │   ├── Payment.java
│                   │   ├── Rental.java
│                   │   └── Vehicle.java
│                   │
│                   ├── service
│                   │   ├── CustomerService.java
│                   │   ├── PaymentService.java
│                   │   ├── RentalService.java
│                   │   ├── ReportService.java
│                   │   └── VehicleService.java
│                   │
│                   └── util
│                       ├── DBConnection.java
│                       └── ValidationUtil.java
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## Database Design

The system uses four main tables:

```text
customers
    │
    │ 1
    │
    │ *
  rentals
    │
    │ 1
    │
    │ *
  payments


vehicles
    │
    │ 1
    │
    │ *
  rentals
```

### Relationships

* One customer can have multiple rentals.
* One vehicle can appear in multiple rental records over time.
* One rental can have multiple payment records.

The complete database schema is available in:

```text
database/schema.sql
```

---

## Transaction Management

Rental creation uses a JDBC transaction to maintain data consistency.

Example flow:

```text
Create Rental
     ↓
Validate Customer
     ↓
Validate Vehicle
     ↓
Check Vehicle Availability
     ↓
Create Rental Record
     ↓
Update Vehicle Status → RENTED
     ↓
COMMIT
```

If an operation fails:

```text
Exception
   ↓
ROLLBACK
   ↓
Database remains consistent
```

Vehicle return follows a similar transaction approach:

```text
Return Vehicle
     ↓
Update Rental Status → COMPLETED
     ↓
Update Vehicle Status → AVAILABLE
     ↓
COMMIT
```

---

## Validation and Exception Handling

The application uses custom exceptions such as:

* `CustomerNotFoundException`
* `VehicleNotFoundException`
* `VehicleNotAvailableException`
* `RentalNotFoundException`
* `PaymentNotFoundException`
* `InvalidPaymentException`

Input validation includes:

* Required text validation
* Positive ID validation
* Phone number validation
* Email validation
* Positive payment amount validation
* Rental rate validation

---

## Reports

The application provides SQL-based reports including:

1. Available Vehicles
2. Active Rentals
3. Completed Rentals
4. Customer Rental History
5. Total Revenue
6. Revenue By Payment Method
7. Most Rented Vehicles
8. Dashboard Summary

These reports demonstrate the use of SQL joins, aggregation, grouping, filtering, and ordering.

---

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/rajtharun5/Vehicle-Rental-Management-System.git
```

### 2. Open the project

Open the project using IntelliJ IDEA or another Java IDE that supports Maven.

### 3. Configure MySQL

Create the database and tables using:

```text
database/schema.sql
```

### 4. Configure database connection

Update the local database connection settings in:

```text
src/main/java/com/vehiclerental/util/DBConnection.java
```

Use your own MySQL username and password.

Do not commit database credentials to GitHub.

### 5. Build the project

Using Maven:

```bash
mvn clean install
```

### 6. Run the application

Run:

```text
src/main/java/com/vehiclerental/main/Main.java
```

---

## Example Business Flow

```text
Add Vehicle
     ↓
Add Customer
     ↓
Create Rental
     ↓
Vehicle Status = RENTED
     ↓
Return Vehicle
     ↓
Rental Status = COMPLETED
     ↓
Vehicle Status = AVAILABLE
     ↓
View Reports
```

---

## Java Concepts Demonstrated

This project demonstrates practical use of:

* Classes and Objects
* Encapsulation
* Constructors
* Getters and Setters
* Object Relationships
* Collections
* Exception Handling
* Custom Exceptions
* JDBC
* PreparedStatement
* ResultSet
* Transactions
* `Connection`
* `BigDecimal`
* `LocalDate`
* Layered Architecture
* DAO Pattern
* Service Layer
* SQL Joins
* SQL Aggregation
* Git and GitHub

---

## Future Enhancements

Possible future improvements include:

* REST API using Spring Boot
* Web-based frontend
* Authentication and authorization
* JWT-based security
* Online payment integration
* Vehicle image management
* Email notifications
* Advanced analytics dashboard
* Docker deployment

---

## Author

**Ramavath Tharun**

Computer Science & Engineering Graduated

GitHub:
https://github.com/rajtharun5
