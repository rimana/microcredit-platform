// Emplacement: src/main/java/com/microcredit/microcreditplatform/model/CreditDecision.java
package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_decisions")
public class CreditDecision {

    public enum DecisionType {
        APPROVE, REJECT, PENDING_INFO, APPROVE_WITH_CONDITIONS
    }

    public enum DecisionStatus {
        DRAFT, FINAL, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_request_id")
    private CreditRequest creditRequest;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User agent;

    @Enumerated(EnumType.STRING)
    private DecisionType decision;

    @Column(length = 1000)
    private String comments;

    private String riskAssessment;

    private Double suggestedAmount;

    private Integer suggestedDuration;

    private LocalDateTime decisionDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private DecisionStatus status = DecisionStatus.FINAL;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CreditRequest getCreditRequest() { return creditRequest; }
    public void setCreditRequest(CreditRequest creditRequest) { this.creditRequest = creditRequest; }

    public User getAgent() { return agent; }
    public void setAgent(User agent) { this.agent = agent; }

    public DecisionType getDecision() { return decision; }
    public void setDecision(DecisionType decision) { this.decision = decision; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public String getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(String riskAssessment) { this.riskAssessment = riskAssessment; }

    public Double getSuggestedAmount() { return suggestedAmount; }
    public void setSuggestedAmount(Double suggestedAmount) { this.suggestedAmount = suggestedAmount; }

    public Integer getSuggestedDuration() { return suggestedDuration; }
    public void setSuggestedDuration(Integer suggestedDuration) { this.suggestedDuration = suggestedDuration; }

    public LocalDateTime getDecisionDate() { return decisionDate; }
    public void setDecisionDate(LocalDateTime decisionDate) { this.decisionDate = decisionDate; }

    public DecisionStatus getStatus() { return status; }
    public void setStatus(DecisionStatus status) { this.status = status; }
}