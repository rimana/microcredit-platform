// Emplacement: src/main/java/com/microcredit/microcreditplatform/dto/RepaymentSimulationDTO.java
package com.microcredit.microcreditplatform.dto;

public class RepaymentSimulationDTO {
    private Double monthlyPayment;
    private Double totalInterest;
    private Double totalAmount;
    private Double interestRate;
    private Integer duration;

    // Getters et Setters
    public Double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(Double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public Double getTotalInterest() { return totalInterest; }
    public void setTotalInterest(Double totalInterest) { this.totalInterest = totalInterest; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
}