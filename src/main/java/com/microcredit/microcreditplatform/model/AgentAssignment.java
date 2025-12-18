// Emplacement: src/main/java/com/microcredit/microcreditplatform/model/AgentAssignment.java
package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_assignments")
public class AgentAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_request_id")
    private CreditRequest creditRequest;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User agent;

    private LocalDateTime assignedDate = LocalDateTime.now();

    private LocalDateTime deadline;

    private Integer priority; // 1-5, 5 étant le plus urgent

    @Column(length = 500)
    private String assignmentReason;

    private String status = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CreditRequest getCreditRequest() { return creditRequest; }
    public void setCreditRequest(CreditRequest creditRequest) { this.creditRequest = creditRequest; }

    public User getAgent() { return agent; }
    public void setAgent(User agent) { this.agent = agent; }

    public LocalDateTime getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDateTime assignedDate) { this.assignedDate = assignedDate; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getAssignmentReason() { return assignmentReason; }
    public void setAssignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}