package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.CreditRequestDTO;
import com.microcredit.microcreditplatform.dto.ScoringResponseDTO;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.security.UserPrincipal;
import com.microcredit.microcreditplatform.service.CreditService;
import com.microcredit.microcreditplatform.service.XGBoostMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @Autowired
    private XGBoostMLService mlService;

    // ==================== NOUVEAUX ENDPOINTS POUR DASHBOARD ====================

    /**
     * ✅ ENDPOINT 1: Pour section "Demandes en Attente" du dashboard
     * Récupère les VRAIES demandes depuis la base de données
     * URL: GET /api/credit/dashboard/pending-requests
     */
    @GetMapping("/dashboard/pending-requests")
    public ResponseEntity<?> getDashboardPendingRequests() {
        try {
            List<Map<String, Object>> requests = creditService.getDashboardPendingRequests();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", requests.size());
            response.put("requests", requests);

            System.out.println("✅ Dashboard: Envoyé " + requests.size() + " demandes");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Erreur dashboard: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "Erreur chargement demandes",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * ✅ ENDPOINT 2: Pour "Analyser ce Crédit"
     * Récupère les détails COMPLETS d'une demande spécifique
     * URL: GET /api/credit/dashboard/analyze/{id}
     */
    @GetMapping("/dashboard/analyze/{id}")
    public ResponseEntity<?> analyzeCreditRequest(@PathVariable Long id) {
        try {
            Map<String, Object> analysis = creditService.getRequestForAnalysis(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("analysis", analysis);
            response.put("message", "Analyse complète disponible");

            System.out.println("✅ Analyse demande #" + id + " envoyée");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "Demande non trouvée",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * ✅ ENDPOINT 3: Pour statistiques du dashboard
     * URL: GET /api/credit/dashboard/statistics
     */
    @GetMapping("/dashboard/statistics")
    public ResponseEntity<?> getDashboardStatistics() {
        try {
            Map<String, Object> stats = creditService.getDashboardStatistics();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("statistics", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "Erreur statistiques",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * ✅ ENDPOINT 4: Pour "Test Complet ML" (simulation)
     * URL: POST /api/credit/dashboard/test-ml
     */
    @PostMapping("/dashboard/test-ml")
    public ResponseEntity<?> testMLScenarios() {
        try {
            // Créer quelques profils de test
            List<Map<String, Object>> testResults = new java.util.ArrayList<>();

            // Profil 1: Bon
            CreditRequestDTO profile1 = new CreditRequestDTO();
            profile1.setAmount(20000.0);
            profile1.setDuration(24);
            profile1.setMonthlyIncome(8000.0);
            profile1.setIsFunctionnaire(true);
            profile1.setEmployed(true);
            profile1.setAge(35);
            profile1.setProfession("Enseignant");
            profile1.setHasGuarantor(true);
            profile1.setPurpose("Achat voiture");

            ScoringResponseDTO result1 = mlService.calculateScore(profile1);
            testResults.add(Map.of(
                    "profile", "Profil idéal (Fonctionnaire)",
                    "score", result1.getScore(),
                    "risk", result1.getRiskLevel(),
                    "recommendation", result1.getRecommendation(),
                    "type", "SIMULATION"
            ));

            // Profil 2: Risqué
            CreditRequestDTO profile2 = new CreditRequestDTO();
            profile2.setAmount(50000.0);
            profile2.setDuration(60);
            profile2.setMonthlyIncome(3000.0);
            profile2.setIsFunctionnaire(false);
            profile2.setEmployed(false);
            profile2.setAge(22);
            profile2.setProfession("Sans emploi");
            profile2.setHasGuarantor(false);
            profile2.setPurpose("Dettes");

            ScoringResponseDTO result2 = mlService.calculateScore(profile2);
            testResults.add(Map.of(
                    "profile", "Profil risqué",
                    "score", result2.getScore(),
                    "risk", result2.getRiskLevel(),
                    "recommendation", result2.getRecommendation(),
                    "type", "SIMULATION"
            ));

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Tests ML complétés",
                    "results", testResults,
                    "isSimulation", true
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "Erreur tests ML",
                            "message", e.getMessage()
                    ));
        }
    }

    // ==================== ENDPOINTS EXISTANTS (NE PAS MODIFIER) ====================

    @PostMapping("/request")
    public ResponseEntity<?> createCreditRequest(
            @RequestBody CreditRequestDTO creditRequestDTO,
            Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            if (!creditRequestDTO.isValidForScoring()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Données incomplètes pour l'analyse");
                error.put("message", "Veuillez remplir tous les champs obligatoires");
                return ResponseEntity.badRequest().body(error);
            }

            // 1. Calculer le score ML
            ScoringResponseDTO scoringResult = mlService.calculateScore(creditRequestDTO);

            // 2. Convertir DTO en Entity avec les résultats du scoring
            CreditRequest creditRequest = convertToEntity(creditRequestDTO, scoringResult);

            // 3. Sauvegarder la demande
            CreditRequest savedRequest = creditService.createCreditRequest(
                    creditRequest,
                    userPrincipal.getUsername()
            );

            // 4. Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("creditRequest", savedRequest);
            response.put("scoringResult", scoringResult);
            response.put("message", "✅ Demande créée avec analyse IA complète");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/simulate")
    public ResponseEntity<?> simulateScoring(@RequestBody CreditRequestDTO creditRequestDTO) {
        try {
            if (!creditRequestDTO.isValidForScoring()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Données incomplètes pour la simulation"));
            }

            ScoringResponseDTO scoringResult = mlService.calculateScore(creditRequestDTO);
            return ResponseEntity.ok(scoringResult);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur lors de la simulation: " + e.getMessage()));
        }
    }

    @PostMapping("/ml/test")
    public ResponseEntity<?> testMLModel(@RequestBody CreditRequestDTO testData) {
        try {
            ScoringResponseDTO result = mlService.calculateScore(testData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur lors du test ML: " + e.getMessage()));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingCreditRequests() {
        try {
            List<CreditRequest> pendingRequests = creditService.getPendingCreditRequests();
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCreditRequestById(@PathVariable Long id) {
        try {
            CreditRequest creditRequest = creditService.getCreditRequestById(id);
            return ResponseEntity.ok(creditRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur: " + e.getMessage()));
        }
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getUserCreditRequests(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<CreditRequest> requests = creditService.getUserCreditRequests(userPrincipal.getUsername());
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllCreditRequests() {
        List<CreditRequest> requests = creditService.getAllCreditRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateCreditRequestStatus(
            @PathVariable Long id,
            @RequestParam CreditRequest.Status status,
            @RequestParam(required = false) String agentNotes,
            Authentication authentication) {

        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String agentUsername = userPrincipal.getUsername();

            CreditRequest updatedRequest = creditService.updateCreditRequestStatus(
                    id, status, agentUsername, agentNotes
            );
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== METHODE PRIVÉE ====================

    private CreditRequest convertToEntity(CreditRequestDTO dto, ScoringResponseDTO scoringResult) {
        CreditRequest entity = new CreditRequest();

        // Champs de base
        entity.setAmount(dto.getAmount());
        entity.setDuration(dto.getDuration());
        entity.setInterestRate(dto.getInterestRate());
        entity.setPurpose(dto.getPurpose());
        entity.setIsFunctionnaire(dto.getIsFunctionnaire());

        // Champs ML
        entity.setMonthlyIncome(dto.getMonthlyIncome());
        entity.setEmployed(dto.getEmployed());
        entity.setAge(dto.getAge());
        entity.setProfession(dto.getProfession());
        entity.setHasGuarantor(dto.getHasGuarantor());

        // Résultats du scoring
        entity.setScore(scoringResult.getScore());
        entity.setRiskLevel(scoringResult.getRiskLevel());
        entity.setProbabilityDefault(scoringResult.getProbabilityDefault());
        entity.setRecommendation(scoringResult.getRecommendation());
        entity.setMaxRecommendedAmount(scoringResult.getMaxRecommendedAmount());
        entity.setSuggestedDuration(scoringResult.getSuggestedDuration());

        // Convertir les listes en String
        if (scoringResult.getRedFlags() != null && !scoringResult.getRedFlags().isEmpty()) {
            entity.setRedFlags(String.join(", ", scoringResult.getRedFlags()));
        }
        if (scoringResult.getPositiveFactors() != null && !scoringResult.getPositiveFactors().isEmpty()) {
            entity.setPositiveFactors(String.join(", ", scoringResult.getPositiveFactors()));
        }

        // Informations garant
        if (Boolean.FALSE.equals(dto.getIsFunctionnaire())) {
            entity.setGuarantorName(dto.getGuarantorName());
            entity.setGuarantorCin(dto.getGuarantorCin());
            entity.setGuarantorPhone(dto.getGuarantorPhone());
            entity.setGuarantorAddress(dto.getGuarantorAddress());
        }

        return entity;
    }
}