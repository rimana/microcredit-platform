package com.microcredit.microcreditplatform.dto;

import java.util.List;

public class ScoringResponseDTO {
    private Integer score;
    private String riskLevel;
    private Double probabilityDefault;
    private String recommendation;
    private Double maxRecommendedAmount;
    private Integer suggestedDuration;
    private List<String> redFlags;
    private List<String> positiveFactors;

    // === GETTERS & SETTERS ===
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public Double getProbabilityDefault() { return probabilityDefault; }
    public void setProbabilityDefault(Double probabilityDefault) { this.probabilityDefault = probabilityDefault; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public Double getMaxRecommendedAmount() { return maxRecommendedAmount; }
    public void setMaxRecommendedAmount(Double maxRecommendedAmount) { this.maxRecommendedAmount = maxRecommendedAmount; }

    public Integer getSuggestedDuration() { return suggestedDuration; }
    public void setSuggestedDuration(Integer suggestedDuration) { this.suggestedDuration = suggestedDuration; }

    public List<String> getRedFlags() { return redFlags; }
    public void setRedFlags(List<String> redFlags) { this.redFlags = redFlags; }

    public List<String> getPositiveFactors() { return positiveFactors; }
    public void setPositiveFactors(List<String> positiveFactors) { this.positiveFactors = positiveFactors; }

    // === CONSTRUCTEURS ===
    public ScoringResponseDTO() {}

    public ScoringResponseDTO(Integer score, String riskLevel, Double probabilityDefault,
                              String recommendation, Double maxRecommendedAmount,
                              Integer suggestedDuration, List<String> redFlags,
                              List<String> positiveFactors) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.probabilityDefault = probabilityDefault;
        this.recommendation = recommendation;
        this.maxRecommendedAmount = maxRecommendedAmount;
        this.suggestedDuration = suggestedDuration;
        this.redFlags = redFlags;
        this.positiveFactors = positiveFactors;
    }

    // === toString() pour débogage ===
    @Override
    public String toString() {
        return "ScoringResponseDTO{" +
                "score=" + score +
                ", riskLevel='" + riskLevel + '\'' +
                ", probabilityDefault=" + probabilityDefault +
                ", recommendation='" + recommendation + '\'' +
                ", maxRecommendedAmount=" + maxRecommendedAmount +
                ", suggestedDuration=" + suggestedDuration +
                '}';
    }
}