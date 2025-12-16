package com.microcredit.microcreditplatform.dto;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long totalUsers;
    private long activeUsers;
    private long pendingCredits;
    private long approvedCredits;
    private long rejectedCredits;
    private long totalCredits;
    private double totalAmount;
    private double pendingAmount;
    private double approvedAmount;
    private double averageLoanAmount;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(AdminStatsDTO stats) {
        this.totalUsers = stats.getTotalUsers() != null ? stats.getTotalUsers() : 0;
        this.activeUsers = stats.getActiveClients() != null ? stats.getActiveClients() : 0;
        this.pendingCredits = stats.getPendingCredits() != null ? stats.getPendingCredits() : 0;
        this.approvedCredits = stats.getApprovedCredits() != null ? stats.getApprovedCredits() : 0;
        this.rejectedCredits = stats.getRejectedCredits() != null ? stats.getRejectedCredits() : 0;
        this.totalCredits = stats.getTotalCredits() != null ? stats.getTotalCredits() : 0;
        this.totalAmount = stats.getTotalAmount() != null ? stats.getTotalAmount() : 0.0;
        this.pendingAmount = stats.getPendingAmount() != null ? stats.getPendingAmount() : 0.0;
        this.approvedAmount = stats.getApprovedAmount() != null ? stats.getApprovedAmount() : 0.0;
        this.averageLoanAmount = stats.getAverageLoanAmount() != null ? stats.getAverageLoanAmount() : 0.0;
    }
}