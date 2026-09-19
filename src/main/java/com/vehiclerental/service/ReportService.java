package com.vehiclerental.service;

import com.vehiclerental.dao.ReportDAO;
import com.vehiclerental.util.ValidationUtil;

import java.sql.SQLException;

public class ReportService {

    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public void showAvailableVehicles() throws SQLException {
        reportDAO.showAvailableVehicles();
    }

    public void showActiveRentals() throws SQLException {
        reportDAO.showActiveRentals();
    }

    public void showCompletedRentals() throws SQLException {
        reportDAO.showCompletedRentals();
    }

    public void showCustomerRentalHistory(int customerId)
            throws SQLException {

        ValidationUtil.requirePositiveId(
                customerId,
                "Customer ID"
        );

        reportDAO.showCustomerRentalHistory(customerId);
    }
    // =========================================================
// TOTAL REVENUE
// =========================================================

    public void showTotalRevenue()
            throws SQLException {

        reportDAO.showTotalRevenue();
    }
    // =========================================================
// REVENUE BY PAYMENT METHOD
// =========================================================

    public void showRevenueByPaymentMethod()
            throws SQLException {

        reportDAO.showRevenueByPaymentMethod();
    }


// =========================================================
// MOST RENTED VEHICLES
// =========================================================

    public void showMostRentedVehicles()
            throws SQLException {

        reportDAO.showMostRentedVehicles();
    }


// =========================================================
// DASHBOARD SUMMARY
// =========================================================

    public void showDashboardSummary()
            throws SQLException {

        reportDAO.showDashboardSummary();
    }
}