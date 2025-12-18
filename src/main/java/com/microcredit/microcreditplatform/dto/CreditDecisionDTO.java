// Emplacement: src/main/java/com/microcredit/microcreditplatform/dto/CreditDecisionDTO.java
package com.microcredit.microcreditplatform.dto;

import java.util.List;

public class CreditDecisionDTO {
    public enum DecisionType {
        APPROVE, REJECT, PENDING_INFO, APPROVE_WITH_CONDITIONS
    }

    private DecisionType decisionType;
    private String comments;
    private String riskAssessment;
    private Double suggestedAmount;
    private Integer suggestedDuration;
    private List<String> conditions;

    // Getters et Setters
    public DecisionType getDecisionType() { return decisionType; }
    public void setDecisionType(DecisionType decisionType) { this.decisionType = decisionType; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public String getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(String riskAssessment) { this.riskAssessment = riskAssessment; }

    public Double getSuggestedAmount() { return suggestedAmount; }
    public void setSuggestedAmount(Double suggestedAmount) { this.suggestedAmount = suggestedAmount; }

    public Integer getSuggestedDuration() { return suggestedDuration; }
    public void setSuggestedDuration(Integer suggestedDuration) { this.suggestedDuration = suggestedDuration; }

    public List<String> getConditions() { return conditions; }
    public void setConditions(List<String> conditions) { this.conditions = conditions; }
}