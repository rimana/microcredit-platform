package com.microcredit.microcreditplatform.dto;

public class MonthlyStatsDTO {
    private Integer month;
    private Integer year;
    private Long creditCount;
    private Double totalAmount;

    // Constructeurs
    public MonthlyStatsDTO() {}

    public MonthlyStatsDTO(Integer month, Integer year, Long creditCount, Double totalAmount) {
        this.month = month;
        this.year = year;
        this.creditCount = creditCount;
        this.totalAmount = totalAmount;
    }

    // Getters et Setters
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Long getCreditCount() { return creditCount; }
    public void setCreditCount(Long creditCount) { this.creditCount = creditCount; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    // Méthode utilitaire
    public String getMonthYear() {
        return String.format("%02d/%d", month, year);
    }
}