package com.microcredit.microcreditplatform.dto;

public class CreditRequestDTO {
    private Double amount;
    private Integer duration;
    private Double interestRate;
    private String purpose;
    private Boolean isFunctionnaire;
    private String guarantorName;
    private String guarantorCin;
    private String guarantorPhone;
    private String guarantorAddress;

    // Getters et Setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public Boolean getIsFunctionnaire() { return isFunctionnaire; }
    public void setIsFunctionnaire(Boolean isFunctionnaire) { this.isFunctionnaire = isFunctionnaire; }

    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }

    public String getGuarantorCin() { return guarantorCin; }
    public void setGuarantorCin(String guarantorCin) { this.guarantorCin = guarantorCin; }

    public String getGuarantorPhone() { return guarantorPhone; }
    public void setGuarantorPhone(String guarantorPhone) { this.guarantorPhone = guarantorPhone; }

    public String getGuarantorAddress() { return guarantorAddress; }
    public void setGuarantorAddress(String guarantorAddress) { this.guarantorAddress = guarantorAddress; }
}