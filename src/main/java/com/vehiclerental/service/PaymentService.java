package com.vehiclerental.service;

import com.vehiclerental.dao.PaymentDAO;
import com.vehiclerental.dao.RentalDAO;
import com.vehiclerental.exception.InvalidPaymentException;
import com.vehiclerental.exception.PaymentNotFoundException;
import com.vehiclerental.exception.RentalNotFoundException;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.Rental;
import com.vehiclerental.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.rentalDAO = new RentalDAO();
    }


    // =========================================================
    // ADD PAYMENT
    // =========================================================

    public void addPayment(
            int rentalId,
            BigDecimal amount,
            String paymentMethod)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                rentalId,
                "Rental ID"
        );

        ValidationUtil.requirePositiveAmount(
                amount,
                "Payment amount"
        );

        ValidationUtil.requireText(
                paymentMethod,
                "Payment method"
        );


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


        // Rental must be completed
        if (!"COMPLETED".equalsIgnoreCase(
                rental.getStatus())) {

            throw new InvalidPaymentException(
                    "Payment can be made only for a completed rental."
            );
        }


        // Payment cannot exceed rental amount
        BigDecimal rentalAmount =
                BigDecimal.valueOf(
                        rental.getTotalAmount()
                );

        if (amount.compareTo(rentalAmount) > 0) {
            throw new InvalidPaymentException(
                    "Payment amount cannot exceed " +
                            rentalAmount
            );
        }


        // Create payment
        Payment payment =
                new Payment(
                        rental,
                        amount,
                        paymentMethod
                );


        // Save payment
        paymentDAO.addPayment(payment);
    }


    // =========================================================
    // GET PAYMENT
    // =========================================================

    public Payment getPaymentById(int paymentId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                paymentId,
                "Payment ID"
        );

        Payment payment =
                paymentDAO.getPaymentById(paymentId);

        if (payment == null) {
            throw new PaymentNotFoundException(
                    "Payment with ID " +
                            paymentId +
                            " was not found."
            );
        }

        return payment;
    }


    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    public List<Payment> getAllPayments()
            throws SQLException {

        return paymentDAO.getAllPayments();
    }


    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    public boolean updatePayment(Payment payment)
            throws SQLException {

        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment cannot be null."
            );
        }

        ValidationUtil.requirePositiveId(
                payment.getPaymentId(),
                "Payment ID"
        );

        ValidationUtil.requirePositiveAmount(
                payment.getAmount(),
                "Payment amount"
        );

        ValidationUtil.requireText(
                payment.getPaymentMethod(),
                "Payment method"
        );

        boolean updated =
                paymentDAO.updatePayment(payment);

        if (!updated) {
            throw new PaymentNotFoundException(
                    "Payment with ID " +
                            payment.getPaymentId() +
                            " was not found."
            );
        }

        return true;
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    public boolean deletePayment(int paymentId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                paymentId,
                "Payment ID"
        );

        boolean deleted =
                paymentDAO.deletePayment(paymentId);

        if (!deleted) {
            throw new PaymentNotFoundException(
                    "Payment with ID " +
                            paymentId +
                            " was not found."
            );
        }

        return true;
    }
}