package com.vehiclerental.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {

    private int paymentId;
    private Rental rental;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDate paymentDate;
    private String paymentStatus;

    public Payment(Rental rental,
                   BigDecimal amount,
                   String paymentMethod) {

        this.rental = rental;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
        this.paymentStatus = "PAID";
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", rental=" + rental +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}