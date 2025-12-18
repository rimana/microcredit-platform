// Emplacement: src/main/java/com/microcredit/microcreditplatform/controller/AgentController.java
package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.*;
import com.microcredit.microcreditplatform.model.AgentAssignment;
import com.microcredit.microcreditplatform.model.CreditDecision;
import com.microcredit.microcreditplatform.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
@PreAuthorize("hasRole('AGENT')")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/dashboard")
    public ResponseEntity<AgentDashboardDTO> getDashboard(Authentication authentication) {
        try {
            String agentUsername = authentication.getName();
            AgentDashboardDTO dashboard = agentService.getAgentDashboard(agentUsername);
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/credits/assigned")
    public ResponseEntity<List<AgentAssignment>> getAssignedCredits(
            Authentication authentication,
            @RequestParam(required = false) String status) {
        try {
            String agentUsername = authentication.getName();
            List<AgentAssignment> credits = agentService.getAssignedCredits(agentUsername, status);
            return ResponseEntity.ok(credits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/analyze/{creditId}")
    public ResponseEntity<ScoringAnalysisDTO> analyzeCredit(
            @PathVariable Long creditId,
            Authentication authentication) {
        try {
            String agentUsername = authentication.getName();
            ScoringAnalysisDTO analysis = agentService.analyzeCreditRequest(creditId, agentUsername);
            return ResponseEntity.ok(analysis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/decision/{creditId}")
    public ResponseEntity<?> makeDecision(
            @PathVariable Long creditId,
            @RequestBody CreditDecisionDTO decisionDTO,
            Authentication authentication) {
        try {
            String agentUsername = authentication.getName();
            CreditDecision decision = agentService.makeDecision(creditId, decisionDTO, agentUsername);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Décision enregistrée avec succès");
            response.put("decisionId", decision.getId());
            response.put("creditStatus", decision.getCreditRequest().getStatus());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de l'enregistrement de la décision"));
        }
    }

    @PostMapping("/assign/{creditId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> assignCredit(
            @PathVariable Long creditId,
            @RequestBody Map<String, Object> request) {
        try {
            Long agentId = Long.valueOf(request.get("agentId").toString());
            String reason = (String) request.get("reason");
            Integer priority = request.get("priority") != null ?
                    Integer.valueOf(request.get("priority").toString()) : 3;

            AgentAssignment assignment = agentService.assignCreditToAgent(
                    creditId, agentId, reason, priority);

            return ResponseEntity.ok(Map.of(
                    "message", "Crédit assigné avec succès",
                    "assignmentId", assignment.getId()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de l'assignation"));
        }
    }

    @GetMapping("/decisions/history")
    public ResponseEntity<List<CreditDecision>> getDecisionHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String agentUsername = authentication.getName();
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Agent Controller is working!",
                "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
}