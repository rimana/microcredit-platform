// Emplacement: src/main/java/com/microcredit/microcreditplatform/dto/ScoringAnalysisDTO.java
package com.microcredit.microcreditplatform.dto;

import com.microcredit.microcreditplatform.model.CreditRequest;
import java.util.List;

public class ScoringAnalysisDTO {
    private Integer creditScore;
    private String riskLevel;
    private List<String> redFlags;
    private List<String> positiveFactors;
    private String recommendations;
    private Double probabilityDefault;
    private Double maxSuggestedAmount;
    private RepaymentSimulationDTO repaymentSimulation;
    private List<CreditRequest> userCreditHistory;

    // Getters et Setters
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public List<String> getRedFlags() { return redFlags; }
    public void setRedFlags(List<String> redFlags) { this.redFlags = redFlags; }

    public List<String> getPositiveFactors() { return positiveFactors; }
    public void setPositiveFactors(List<String> positiveFactors) { this.positiveFactors = positiveFactors; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public Double getProbabilityDefault() { return probabilityDefault; }
    public void setProbabilityDefault(Double probabilityDefault) { this.probabilityDefault = probabilityDefault; }

    public Double getMaxSuggestedAmount() { return maxSuggestedAmount; }
    public void setMaxSuggestedAmount(Double maxSuggestedAmount) { this.maxSuggestedAmount = maxSuggestedAmount; }

    public RepaymentSimulationDTO getRepaymentSimulation() { return repaymentSimulation; }
    public void setRepaymentSimulation(RepaymentSimulationDTO repaymentSimulation) { this.repaymentSimulation = repaymentSimulation; }

    public List<CreditRequest> getUserCreditHistory() { return userCreditHistory; }
    public void setUserCreditHistory(List<CreditRequest> userCreditHistory) { this.userCreditHistory = userCreditHistory; }
}