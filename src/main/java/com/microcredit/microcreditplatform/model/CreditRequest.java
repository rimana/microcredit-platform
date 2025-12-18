package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_requests")
public class CreditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === CHAMPS DE BASE ===
    private Double amount;
    private Integer duration;
    private Double interestRate;
    private String purpose;
    private Boolean isFunctionnaire = false;

    // === CHAMPS REQUIS POUR LE SCORING ML ===
    private Double monthlyIncome;
    private Boolean employed;
    private Integer age;
    private String profession;
    private Boolean hasGuarantor;

    // === RÉSULTATS DU SCORING ML ===
    private Integer score;
    private String riskLevel;
    private Double probabilityDefault;
    private String recommendation;
    private String redFlags;
    private String positiveFactors;
    private Double maxRecommendedAmount;
    private Integer suggestedDuration;

    // === INFORMATIONS GARANT ===
    private String guarantorName;
    private String guarantorCin;
    private String guarantorPhone;
    private String guarantorAddress;

    // === FICHIERS ===
    private String idCardFilePath;
    private String photosFilePath;
    private String salaryCertificatePath;
    private String workCertificatePath;
    private String guarantorFilesPath;

    // === STATUT ===
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    // === MÉTADONNÉES ===
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String agentNotes;

    // === RELATIONS ===
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Status {
        PENDING,
        IN_REVIEW,
        APPROVED,
        REJECTED,
        CANCELLED,
        PAID
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = Status.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // === GETTERS & SETTERS ===
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

    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Boolean getEmployed() { return employed; }
    public void setEmployed(Boolean employed) { this.employed = employed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public Boolean getHasGuarantor() { return hasGuarantor; }
    public void setHasGuarantor(Boolean hasGuarantor) { this.hasGuarantor = hasGuarantor; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public Double getProbabilityDefault() { return probabilityDefault; }
    public void setProbabilityDefault(Double probabilityDefault) { this.probabilityDefault = probabilityDefault; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getRedFlags() { return redFlags; }
    public void setRedFlags(String redFlags) { this.redFlags = redFlags; }

    public String getPositiveFactors() { return positiveFactors; }
    public void setPositiveFactors(String positiveFactors) { this.positiveFactors = positiveFactors; }

    public Double getMaxRecommendedAmount() { return maxRecommendedAmount; }
    public void setMaxRecommendedAmount(Double maxRecommendedAmount) { this.maxRecommendedAmount = maxRecommendedAmount; }

    public Integer getSuggestedDuration() { return suggestedDuration; }
    public void setSuggestedDuration(Integer suggestedDuration) { this.suggestedDuration = suggestedDuration; }

    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }

    public String getGuarantorCin() { return guarantorCin; }
    public void setGuarantorCin(String guarantorCin) { this.guarantorCin = guarantorCin; }

    public String getGuarantorPhone() { return guarantorPhone; }
    public void setGuarantorPhone(String guarantorPhone) { this.guarantorPhone = guarantorPhone; }

    public String getGuarantorAddress() { return guarantorAddress; }
    public void setGuarantorAddress(String guarantorAddress) { this.guarantorAddress = guarantorAddress; }

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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getAgentNotes() { return agentNotes; }
    public void setAgentNotes(String agentNotes) { this.agentNotes = agentNotes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}