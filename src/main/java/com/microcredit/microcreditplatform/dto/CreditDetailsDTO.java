package com.microcredit.microcreditplatform.dto;

import com.microcredit.microcreditplatform.model.CreditRequest;

import java.time.LocalDateTime;

public class CreditDetailsDTO {
    private Long id;
    private Double amount;
    private Integer duration;
    private Double interestRate;
    private String purpose;
    private Boolean isFunctionnaire;
    private CreditRequest.Status status;
    private LocalDateTime createdAt;
    
    // Informations utilisateur
    private String clientUsername;
    private String clientEmail;
    private String clientPhone;
    private String clientCin;
    
    // Informations garant (si applicable)
    private String guarantorName;
    private String guarantorCin;
    private String guarantorPhone;
    private String guarantorAddress;

    // Constructeurs
    public CreditDetailsDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public CreditRequest.Status getStatus() { return status; }
    public void setStatus(CreditRequest.Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getClientUsername() { return clientUsername; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }

    public String getClientCin() { return clientCin; }
    public void setClientCin(String clientCin) { this.clientCin = clientCin; }

    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }

    public String getGuarantorCin() { return guarantorCin; }
    public void setGuarantorCin(String guarantorCin) { this.guarantorCin = guarantorCin; }

    public String getGuarantorPhone() { return guarantorPhone; }
    public void setGuarantorPhone(String guarantorPhone) { this.guarantorPhone = guarantorPhone; }

    public String getGuarantorAddress() { return guarantorAddress; }
    public void setGuarantorAddress(String guarantorAddress) { this.guarantorAddress = guarantorAddress; }
}