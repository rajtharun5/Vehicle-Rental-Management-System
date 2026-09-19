package com.vehiclerental.util;

import java.math.BigDecimal;

public class ValidationUtil {

    private ValidationUtil() {
        // Utility class
    }


    // =========================================================
    // STRING VALIDATION
    // =========================================================

    public static void requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }
    }


    // =========================================================
    // ID VALIDATION
    // =========================================================

    public static void requirePositiveId(
            int id,
            String fieldName) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    fieldName +
                            " must be greater than zero."
            );
        }
    }


    // =========================================================
    // PHONE VALIDATION
    // =========================================================

    public static void validatePhone(String phone) {

        requireText(phone, "Phone number");

        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                    "Phone number must contain exactly 10 digits."
            );
        }
    }


    // =========================================================
    // EMAIL VALIDATION
    // =========================================================

    public static void validateEmail(String email) {

        if (email == null || email.isBlank()) {
            return;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "Invalid email address."
            );
        }
    }


    // =========================================================
    // POSITIVE AMOUNT VALIDATION
    // =========================================================

    public static void requirePositiveAmount(
            BigDecimal amount,
            String fieldName) {

        if (amount == null) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    fieldName +
                            " must be greater than zero."
            );
        }
    }


    // =========================================================
    // POSITIVE DOUBLE VALIDATION
    // =========================================================

    public static void requirePositiveAmount(
            double amount,
            String fieldName) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    fieldName +
                            " must be greater than zero."
            );
        }
    }
}