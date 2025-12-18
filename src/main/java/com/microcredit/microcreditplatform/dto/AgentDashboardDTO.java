// Emplacement: src/main/java/com/microcredit/microcreditplatform/dto/AgentDashboardDTO.java
package com.microcredit.microcreditplatform.dto;

public class AgentDashboardDTO {
    private Integer totalAssigned;
    private Integer pendingReview;
    private Integer decisionsThisMonth;
    private Double approvalRate;
    private Integer highPriorityAlerts;
    private Integer upcomingDeadlines;
    private Double averageProcessingTime;

    // Constructeur
    public AgentDashboardDTO() {}

    // Getters et Setters
    public Integer getTotalAssigned() { return totalAssigned; }
    public void setTotalAssigned(Integer totalAssigned) { this.totalAssigned = totalAssigned; }

    public Integer getPendingReview() { return pendingReview; }
    public void setPendingReview(Integer pendingReview) { this.pendingReview = pendingReview; }

    public Integer getDecisionsThisMonth() { return decisionsThisMonth; }
    public void setDecisionsThisMonth(Integer decisionsThisMonth) { this.decisionsThisMonth = decisionsThisMonth; }

    public Double getApprovalRate() { return approvalRate; }
    public void setApprovalRate(Double approvalRate) { this.approvalRate = approvalRate; }

    public Integer getHighPriorityAlerts() { return highPriorityAlerts; }
    public void setHighPriorityAlerts(Integer highPriorityAlerts) { this.highPriorityAlerts = highPriorityAlerts; }

    public Integer getUpcomingDeadlines() { return upcomingDeadlines; }
    public void setUpcomingDeadlines(Integer upcomingDeadlines) { this.upcomingDeadlines = upcomingDeadlines; }

    public Double getAverageProcessingTime() { return averageProcessingTime; }
    public void setAverageProcessingTime(Double averageProcessingTime) { this.averageProcessingTime = averageProcessingTime; }
}