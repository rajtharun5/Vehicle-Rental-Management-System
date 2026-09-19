-- ============================================================
-- Vehicle Rental Management System
-- Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS vehicle_rental_db;

USE vehicle_rental_db;


-- ============================================================
-- VEHICLES TABLE
-- ============================================================

CREATE TABLE IF NOT EXISTS vehicles (
                                        vehicle_id INT NOT NULL AUTO_INCREMENT,
                                        vehicle_number VARCHAR(20) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    rental_rate DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',

    PRIMARY KEY (vehicle_id),
    UNIQUE KEY vehicle_number (vehicle_number)
    );


-- ============================================================
-- CUSTOMERS TABLE
-- ============================================================

CREATE TABLE IF NOT EXISTS customers (
                                         customer_id INT NOT NULL AUTO_INCREMENT,
                                         name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) DEFAULT NULL,
    license_number VARCHAR(50) NOT NULL,

    PRIMARY KEY (customer_id),
    UNIQUE KEY license_number (license_number)
    );


-- ============================================================
-- RENTALS TABLE
-- ============================================================

CREATE TABLE IF NOT EXISTS rentals (
                                       rental_id INT NOT NULL AUTO_INCREMENT,
                                       customer_id INT NOT NULL,
                                       vehicle_id INT NOT NULL,
                                       rental_date DATE NOT NULL,
                                       expected_return_date DATE NOT NULL,
                                       actual_return_date DATE DEFAULT NULL,
                                       total_amount DECIMAL(10,2) DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    PRIMARY KEY (rental_id),

    KEY fk_rental_customer (customer_id),
    KEY fk_rental_vehicle (vehicle_id),

    CONSTRAINT fk_rental_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers (customer_id),

    CONSTRAINT fk_rental_vehicle
    FOREIGN KEY (vehicle_id)
    REFERENCES vehicles (vehicle_id)
    );


-- ============================================================
-- PAYMENTS TABLE
-- ============================================================

CREATE TABLE IF NOT EXISTS payments (
                                        payment_id INT NOT NULL AUTO_INCREMENT,
                                        rental_id INT NOT NULL,
                                        amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_date DATE NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PAID',

    PRIMARY KEY (payment_id),

    KEY fk_payment_rental (rental_id),

    CONSTRAINT fk_payment_rental
    FOREIGN KEY (rental_id)
    REFERENCES rentals (rental_id)
    );


-- ============================================================
-- DATABASE RELATIONSHIPS
-- ============================================================
--
-- customers 1 -------- * rentals
-- vehicles  1 -------- * rentals
-- rentals   1 -------- * payments
--
-- ============================================================