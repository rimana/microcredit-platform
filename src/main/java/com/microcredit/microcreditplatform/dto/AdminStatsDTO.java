package com.microcredit.microcreditplatform.dto;

public class AdminStatsDTO {
    // Statistiques utilisateurs
    private Long totalUsers;
    private Long activeClients;
    private Long totalAgents;
    private Long totalAdmins;
    
    // Statistiques crédits
    private Long totalCredits;
    private Long pendingCredits;
    private Long approvedCredits;
    private Long rejectedCredits;
    
    // Statistiques financières
    private Double totalAmount;
    private Double pendingAmount;
    private Double approvedAmount;
    private Double defaultRate;
    private Double averageLoanAmount;
    
    // Constructeurs
    public AdminStatsDTO() {}

    // Getters et Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getActiveClients() { return activeClients; }
    public void setActiveClients(Long activeClients) { this.activeClients = activeClients; }

    public Long getTotalAgents() { return totalAgents; }
    public void setTotalAgents(Long totalAgents) { this.totalAgents = totalAgents; }

    public Long getTotalAdmins() { return totalAdmins; }
    public void setTotalAdmins(Long totalAdmins) { this.totalAdmins = totalAdmins; }

    public Long getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Long totalCredits) { this.totalCredits = totalCredits; }

    public Long getPendingCredits() { return pendingCredits; }
    public void setPendingCredits(Long pendingCredits) { this.pendingCredits = pendingCredits; }

    public Long getApprovedCredits() { return approvedCredits; }
    public void setApprovedCredits(Long approvedCredits) { this.approvedCredits = approvedCredits; }

    public Long getRejectedCredits() { return rejectedCredits; }
    public void setRejectedCredits(Long rejectedCredits) { this.rejectedCredits = rejectedCredits; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getPendingAmount() { return pendingAmount; }
    public void setPendingAmount(Double pendingAmount) { this.pendingAmount = pendingAmount; }

    public Double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(Double approvedAmount) { this.approvedAmount = approvedAmount; }

    public Double getDefaultRate() { return defaultRate; }
    public void setDefaultRate(Double defaultRate) { this.defaultRate = defaultRate; }

    public Double getAverageLoanAmount() { return averageLoanAmount; }
    public void setAverageLoanAmount(Double averageLoanAmount) { this.averageLoanAmount = averageLoanAmount; }
}