package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings")
public class SystemSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double defaultInterestRate = 12.0;
    private Double maxLoanAmount = 50000.0;
    private Double minLoanAmount = 1000.0;
    private Integer maxLoanDuration = 60; // en mois
    private Integer minLoanDuration = 6; // en mois
    private Boolean systemMaintenance = false;
    private String maintenanceMessage;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructeurs
    public SystemSettings() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getDefaultInterestRate() { return defaultInterestRate; }
    public void setDefaultInterestRate(Double defaultInterestRate) { 
        this.defaultInterestRate = defaultInterestRate;
        this.updatedAt = LocalDateTime.now();
    }

    public Double getMaxLoanAmount() { return maxLoanAmount; }
    public void setMaxLoanAmount(Double maxLoanAmount) { 
        this.maxLoanAmount = maxLoanAmount;
        this.updatedAt = LocalDateTime.now();
    }

    public Double getMinLoanAmount() { return minLoanAmount; }
    public void setMinLoanAmount(Double minLoanAmount) { 
        this.minLoanAmount = minLoanAmount;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getMaxLoanDuration() { return maxLoanDuration; }
    public void setMaxLoanDuration(Integer maxLoanDuration) { 
        this.maxLoanDuration = maxLoanDuration;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getMinLoanDuration() { return minLoanDuration; }
    public void setMinLoanDuration(Integer minLoanDuration) { 
        this.minLoanDuration = minLoanDuration;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getSystemMaintenance() { return systemMaintenance; }
    public void setSystemMaintenance(Boolean systemMaintenance) { 
        this.systemMaintenance = systemMaintenance;
        this.updatedAt = LocalDateTime.now();
    }

    public String getMaintenanceMessage() { return maintenanceMessage; }
    public void setMaintenanceMessage(String maintenanceMessage) { 
        this.maintenanceMessage = maintenanceMessage;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}