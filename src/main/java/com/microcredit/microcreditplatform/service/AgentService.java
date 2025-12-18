// Emplacement: src/main/java/com/microcredit/microcreditplatform/service/AgentService.java
package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.*;
import com.microcredit.microcreditplatform.model.*;
import com.microcredit.microcreditplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AgentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @Autowired
    private CreditDecisionRepository creditDecisionRepository;

    @Autowired
    private AgentAssignmentRepository agentAssignmentRepository;

    @Autowired
    private XGBoostMLService mlService;

    public AgentDashboardDTO getAgentDashboard(String agentUsername) {
        User agent = userRepository.findByUsername(agentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        AgentDashboardDTO dashboard = new AgentDashboardDTO();

        List<AgentAssignment> assignments = agentAssignmentRepository
                .findByAgentAndStatus(agent, "ACTIVE");

        dashboard.setTotalAssigned(assignments.size());
        dashboard.setPendingReview((int) assignments.stream()
                .filter(a -> a.getCreditRequest().getStatus() == CreditRequest.Status.PENDING)
                .count());

        List<CreditDecision> decisions = creditDecisionRepository
                .findByAgentAndDecisionDateAfter(agent, LocalDateTime.now().minusMonths(1));

        long totalDecisions = decisions.size();
        long approved = decisions.stream()
                .filter(d -> d.getDecision() == CreditDecision.DecisionType.APPROVE)
                .count();

        dashboard.setDecisionsThisMonth((int) totalDecisions);
        dashboard.setApprovalRate(totalDecisions > 0 ? (approved * 100.0 / totalDecisions) : 0.0);

        dashboard.setHighPriorityAlerts((int) assignments.stream()
                .filter(a -> a.getPriority() >= 4)
                .count());

        List<AgentAssignment> upcomingDeadlines = assignments.stream()
                .filter(a -> a.getDeadline() != null &&
                        a.getDeadline().isBefore(LocalDateTime.now().plusDays(3)))
                .sorted(Comparator.comparing(AgentAssignment::getDeadline))
                .limit(5)
                .collect(Collectors.toList());

        dashboard.setUpcomingDeadlines(upcomingDeadlines.size());
        dashboard.setAverageProcessingTime(24.5); // Valeur par défaut

        return dashboard;
    }

    public ScoringAnalysisDTO analyzeCreditRequest(Long creditId, String agentUsername) {
        CreditRequest credit = creditRequestRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédit non trouvé"));

        User agent = userRepository.findByUsername(agentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        if (!isAssignedToAgent(credit, agent)) {
            throw new RuntimeException("Non autorisé - crédit non assigné à cet agent");
        }

        ScoringRequestDTO scoringRequest = new ScoringRequestDTO();
        scoringRequest.setMonthlyIncome(credit.getUser().getMonthlyIncome());
        scoringRequest.setLoanAmount(credit.getAmount());
        scoringRequest.setLoanDuration(credit.getDuration());
        scoringRequest.setIsFunctionnaire(credit.getIsFunctionnaire());
        scoringRequest.setEmployed(credit.getUser().getEmployed());
        scoringRequest.setHasGuarantor(credit.getGuarantorName() != null);

        ScoringResponseDTO scoringResult = mlService.calculateScore(scoringRequest);

        ScoringAnalysisDTO analysis = new ScoringAnalysisDTO();
        analysis.setCreditScore(scoringResult.getScore());
        analysis.setRiskLevel(scoringResult.getRiskLevel());
        analysis.setRedFlags(scoringResult.getRedFlags());
        analysis.setRecommendations(scoringResult.getRecommendation());
        analysis.setMaxSuggestedAmount(scoringResult.getMaxRecommendedAmount());
        analysis.setProbabilityDefault(scoringResult.getProbabilityDefault());
        analysis.setRepaymentSimulation(simulateRepayment(credit));
        analysis.setUserCreditHistory(getUserCreditHistory(credit.getUser()));

        return analysis;
    }

    public CreditDecision makeDecision(Long creditId, CreditDecisionDTO decisionDTO, String agentUsername) {
        CreditRequest credit = creditRequestRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédit non trouvé"));

        User agent = userRepository.findByUsername(agentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        if (credit.getStatus() != CreditRequest.Status.PENDING) {
            throw new RuntimeException("Seuls les crédits en attente peuvent être traités");
        }

        if (!isAssignedToAgent(credit, agent)) {
            throw new RuntimeException("Non autorisé");
        }

        CreditDecision decision = new CreditDecision();
        decision.setCreditRequest(credit);
        decision.setAgent(agent);
        decision.setComments(decisionDTO.getComments());
        decision.setRiskAssessment(decisionDTO.getRiskAssessment());
        decision.setSuggestedAmount(decisionDTO.getSuggestedAmount());
        decision.setSuggestedDuration(decisionDTO.getSuggestedDuration());
        decision.setDecisionDate(LocalDateTime.now());
        decision.setStatus(CreditDecision.DecisionStatus.FINAL);

        switch (decisionDTO.getDecisionType()) {
            case APPROVE:
                credit.setStatus(CreditRequest.Status.APPROVED);
                break;
            case REJECT:
                credit.setStatus(CreditRequest.Status.REJECTED);
                break;
            case PENDING_INFO:
                credit.setStatus(CreditRequest.Status.PENDING);
                break;
            default:
                credit.setStatus(CreditRequest.Status.PENDING);
        }

        creditRequestRepository.save(credit);
        return creditDecisionRepository.save(decision);
    }

    public List<AgentAssignment> getAssignedCredits(String agentUsername, String statusFilter) {
        User agent = userRepository.findByUsername(agentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        List<AgentAssignment> assignments = agentAssignmentRepository.findByAgent(agent);

        if ("PENDING".equals(statusFilter)) {
            return assignments.stream()
                    .filter(a -> a.getCreditRequest().getStatus() == CreditRequest.Status.PENDING)
                    .collect(Collectors.toList());
        }

        return assignments;
    }

    public AgentAssignment assignCreditToAgent(Long creditId, Long agentId, String reason, Integer priority) {
        CreditRequest credit = creditRequestRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédit non trouvé"));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        if (agent.getRole() != User.Role.AGENT) {
            throw new RuntimeException("L'utilisateur n'est pas un agent");
        }

        Optional<AgentAssignment> existing = agentAssignmentRepository
                .findByCreditRequestAndStatus(credit, "ACTIVE");

        if (existing.isPresent()) {
            throw new RuntimeException("Ce crédit est déjà assigné à un agent");
        }

        AgentAssignment assignment = new AgentAssignment();
        assignment.setCreditRequest(credit);
        assignment.setAgent(agent);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setDeadline(LocalDateTime.now().plusDays(3));
        assignment.setPriority(priority != null ? priority : 3);
        assignment.setAssignmentReason(reason);
        assignment.setStatus("ACTIVE");

        return agentAssignmentRepository.save(assignment);
    }

    private boolean isAssignedToAgent(CreditRequest credit, User agent) {
        return agent.getRole() == User.Role.AGENT;
    }

    private RepaymentSimulationDTO simulateRepayment(CreditRequest credit) {
        RepaymentSimulationDTO simulation = new RepaymentSimulationDTO();

        double monthlyRate = credit.getInterestRate() / 100 / 12;
        double monthlyPayment = credit.getAmount() * monthlyRate *
                Math.pow(1 + monthlyRate, credit.getDuration()) /
                (Math.pow(1 + monthlyRate, credit.getDuration()) - 1);

        simulation.setMonthlyPayment(monthlyPayment);
        simulation.setTotalInterest(monthlyPayment * credit.getDuration() - credit.getAmount());
        simulation.setTotalAmount(monthlyPayment * credit.getDuration());
        simulation.setInterestRate(credit.getInterestRate());
        simulation.setDuration(credit.getDuration());

        return simulation;
    }

    private List<CreditRequest> getUserCreditHistory(User user) {
        return creditRequestRepository.findByUser(user);
    }


}