package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_requests")
public class CreditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Integer duration;
    private Double interestRate;
    private String purpose;

    // NOUVEAU: Champ pour savoir si c'est un fonctionnaire
    private Boolean isFunctionnaire = false;

    // Informations garant (seulement si non fonctionnaire)
    private String guarantorName;
    private String guarantorCin;
    private String guarantorPhone;
    private String guarantorAddress;

    // NOUVEAU: Chemins des fichiers uploadés
    private String idCardFilePath;        // Carte nationale
    private String photosFilePath;        // 2 photos
    private String salaryCertificatePath; // Attestation de salaire
    private String workCertificatePath;   // Attestation de travail
    private String guarantorFilesPath;    // Documents du garant

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Status {
        PENDING, APPROVED, REJECTED, PAID
    }

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

    // NOUVEAU: Getters et Setters pour isFunctionnaire
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

    // NOUVEAU: Getters et Setters pour les fichiers
    public String getIdCardFilePath() { return idCardFilePath; }
    public void setIdCardFilePath(String idCardFilePath) { this.idCardFilePath = idCardFilePath; }

    public String getPhotosFilePath() { return photosFilePath; }
    public void setPhotosFilePath(String photosFilePath) { this.photosFilePath = photosFilePath; }

    public String getSalaryCertificatePath() { return salaryCertificatePath; }
    public void setSalaryCertificatePath(String salaryCertificatePath) { this.salaryCertificatePath = salaryCertificatePath; }

    public String getWorkCertificatePath() { return workCertificatePath; }
    public void setWorkCertificatePath(String workCertificatePath) { this.workCertificatePath = workCertificatePath; }

    public String getGuarantorFilesPath() { return guarantorFilesPath; }
    public void setGuarantorFilesPath(String guarantorFilesPath) { this.guarantorFilesPath = guarantorFilesPath; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}