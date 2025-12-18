package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.CreditRequestDTO;
import com.microcredit.microcreditplatform.dto.ScoringResponseDTO;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.repository.CreditRequestRepository;
import com.microcredit.microcreditplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreditService {

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private XGBoostMLService mlService;

    // ==================== MÉTHODES POUR RÉCUPÉRER LES VRAIES DONNÉES ====================

    /**
     * ✅ Récupère les VRAIES demandes en attente depuis la base de données
     * Format: Pour afficher dans le dashboard
     */
    public List<Map<String, Object>> getDashboardPendingRequests() {
        // Récupérer les demandes PENDING avec scoring depuis la base
        List<CreditRequest> realRequests = creditRequestRepository.findPendingWithScoring();

        System.out.println("📊 Demandes réelles trouvées en base: " + realRequests.size());

        // Transformer en format pour le frontend
        return realRequests.stream()
                .map(request -> {
                    Map<String, Object> item = new HashMap<>();

                    // Informations de base
                    item.put("id", request.getId());
                    item.put("amount", request.getAmount());
                    item.put("duration", request.getDuration());
                    item.put("purpose", request.getPurpose());
                    item.put("createdAt", formatDate(request.getCreatedAt()));

                    // Récupérer le nom du client depuis User (sans modifier User)
                    User client = request.getUser();
                    if (client != null) {
                        // Utiliser le champ disponible (username ou autre)
                        String clientName = client.getUsername(); // Vous pouvez changer par un autre champ
                        item.put("clientName", clientName != null ? clientName : "Client");
                        item.put("clientEmail", client.getEmail() != null ? client.getEmail() : "");
                    } else {
                        item.put("clientName", "Client");
                        item.put("clientEmail", "");
                    }

                    // Données de scoring (RÉELLES)
                    item.put("score", request.getScore());
                    item.put("riskLevel", request.getRiskLevel());
                    item.put("probabilityDefault", request.getProbabilityDefault());
                    item.put("recommendation", request.getRecommendation());
                    item.put("maxRecommendedAmount", request.getMaxRecommendedAmount());
                    item.put("suggestedDuration", request.getSuggestedDuration());

                    // Données ML
                    item.put("monthlyIncome", request.getMonthlyIncome());
                    item.put("profession", request.getProfession());
                    item.put("age", request.getAge());
                    item.put("isFunctionnaire", request.getIsFunctionnaire());
                    item.put("employed", request.getEmployed());
                    item.put("hasGuarantor", request.getHasGuarantor());

                    // Flags
                    if (request.getRedFlags() != null) {
                        item.put("redFlags", request.getRedFlags());
                    }
                    if (request.getPositiveFactors() != null) {
                        item.put("positiveFactors", request.getPositiveFactors());
                    }

                    // Debug
                    System.out.println("  📄 Demande #" + request.getId() +
                            " - Client: " + item.get("clientName") +
                            " - " + request.getAmount() + " DH" +
                            " - Score: " + request.getScore());

                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * ✅ Récupère une demande spécifique pour analyse
     */
    public Map<String, Object> getRequestForAnalysis(Long requestId) {
        CreditRequest request = creditRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        Map<String, Object> analysis = new HashMap<>();

        // Informations de base
        analysis.put("id", request.getId());
        analysis.put("amount", request.getAmount());
        analysis.put("duration", request.getDuration());
        analysis.put("purpose", request.getPurpose());
        analysis.put("createdAt", formatDate(request.getCreatedAt()));
        analysis.put("status", request.getStatus().toString());

        // Informations client
        User client = request.getUser();
        if (client != null) {
            analysis.put("clientUsername", client.getUsername());
            analysis.put("clientEmail", client.getEmail());
            analysis.put("clientPhone", client.getPhone());
            analysis.put("clientProfession", client.getProfession());
            analysis.put("clientMonthlyIncome", client.getMonthlyIncome());
        }

        // Données ML de la demande
        analysis.put("monthlyIncome", request.getMonthlyIncome());
        analysis.put("profession", request.getProfession());
        analysis.put("age", request.getAge());
        analysis.put("isFunctionnaire", request.getIsFunctionnaire());
        analysis.put("employed", request.getEmployed());
        analysis.put("hasGuarantor", request.getHasGuarantor());

        // Résultats du scoring
        analysis.put("score", request.getScore());
        analysis.put("riskLevel", request.getRiskLevel());
        analysis.put("probabilityDefault", request.getProbabilityDefault());
        analysis.put("recommendation", request.getRecommendation());
        analysis.put("maxRecommendedAmount", request.getMaxRecommendedAmount());
        analysis.put("suggestedDuration", request.getSuggestedDuration());

        // Flags
        if (request.getRedFlags() != null) {
            String[] flags = request.getRedFlags().split(", ");
            analysis.put("redFlags", flags);
        }

        if (request.getPositiveFactors() != null) {
            String[] factors = request.getPositiveFactors().split(", ");
            analysis.put("positiveFactors", factors);
        }

        // Informations garant
        if (Boolean.FALSE.equals(request.getIsFunctionnaire())) {
            Map<String, String> guarantor = new HashMap<>();
            guarantor.put("name", request.getGuarantorName());
            guarantor.put("cin", request.getGuarantorCin());
            guarantor.put("phone", request.getGuarantorPhone());
            guarantor.put("address", request.getGuarantorAddress());
            analysis.put("guarantor", guarantor);
        }

        return analysis;
    }

    /**
     * ✅ Récupère les statistiques réelles
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Comptes
        long pendingCount = creditRequestRepository.countByStatus(CreditRequest.Status.PENDING);
        long totalCount = creditRequestRepository.count();

        stats.put("pendingCount", pendingCount);
        stats.put("totalCount", totalCount);

        // Statistiques scoring
        Object[] scoringStats = creditRequestRepository.getDashboardStats();
        if (scoringStats != null && scoringStats.length >= 4) {
            stats.put("scoredRequests", scoringStats[0]);
            stats.put("averageScore", scoringStats[1] != null ?
                    String.format("%.1f", scoringStats[1]) : "0.0");
            stats.put("minScore", scoringStats[2]);
            stats.put("maxScore", scoringStats[3]);
        }

        // Niveaux de risque
        List<CreditRequest> lowRisk = creditRequestRepository.findByRiskLevel("FAIBLE");
        List<CreditRequest> mediumRisk = creditRequestRepository.findByRiskLevel("MOYEN");
        List<CreditRequest> highRisk = creditRequestRepository.findByRiskLevel("ÉLEVÉ");

        stats.put("lowRiskCount", lowRisk.size());
        stats.put("mediumRiskCount", mediumRisk.size());
        stats.put("highRiskCount", highRisk.size());

        // Dernières demandes
        List<CreditRequest> recent = creditRequestRepository.findPendingWithScoring();
        if (!recent.isEmpty()) {
            stats.put("lastRequestDate", formatDate(recent.get(0).getCreatedAt()));
            stats.put("lastRequestAmount", recent.get(0).getAmount());
        }

        System.out.println("📈 Statistiques dashboard:");
        System.out.println("  - En attente: " + pendingCount);
        System.out.println("  - Score moyen: " + stats.get("averageScore"));
        System.out.println("  - Haut risque: " + highRisk.size());

        return stats;
    }

    /**
     * ✅ Formatte la date
     */
    private String formatDate(LocalDateTime date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }

    // ==================== MÉTHODES EXISTANTES ====================

    @Transactional
    public CreditRequest createCreditRequest(CreditRequest creditRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        validateCreditRequest(creditRequest);
        creditRequest.setUser(user);

        System.out.println("✅ Demande créée pour " + user.getUsername() +
                " - Score: " + creditRequest.getScore());

        return creditRequestRepository.save(creditRequest);
    }

    public List<CreditRequest> getUserCreditRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return creditRequestRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<CreditRequest> getAllCreditRequests() {
        return creditRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<CreditRequest> getPendingCreditRequests() {
        return creditRequestRepository.findByStatusOrderByCreatedAtDesc(
                CreditRequest.Status.PENDING
        );
    }

    public CreditRequest getCreditRequestById(Long id) {
        return creditRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande de crédit non trouvée"));
    }

    @Transactional
    public CreditRequest updateCreditRequestStatus(
            Long id,
            CreditRequest.Status status,
            String agentUsername,
            String agentNotes) {

        CreditRequest creditRequest = getCreditRequestById(id);
        creditRequest.setStatus(status);
        creditRequest.setReviewedBy(agentUsername);
        creditRequest.setReviewedAt(LocalDateTime.now());

        if (agentNotes != null && !agentNotes.trim().isEmpty()) {
            creditRequest.setAgentNotes(agentNotes);
        }

        return creditRequestRepository.save(creditRequest);
    }

    private void validateCreditRequest(CreditRequest creditRequest) {
        if (creditRequest.getAmount() == null || creditRequest.getAmount() <= 0) {
            throw new RuntimeException("Le montant doit être positif");
        }

        if (creditRequest.getDuration() == null || creditRequest.getDuration() <= 0) {
            throw new RuntimeException("La durée doit être positive");
        }

        if (creditRequest.getMonthlyIncome() == null || creditRequest.getMonthlyIncome() <= 0) {
            throw new RuntimeException("Le revenu mensuel est requis");
        }

        if (creditRequest.getAge() == null || creditRequest.getAge() < 18) {
            throw new RuntimeException("L'âge doit être au moins 18 ans");
        }

        if (creditRequest.getProfession() == null || creditRequest.getProfession().trim().isEmpty()) {
            throw new RuntimeException("La profession est requise");
        }

        if (Boolean.FALSE.equals(creditRequest.getIsFunctionnaire())) {
            if (creditRequest.getGuarantorName() == null || creditRequest.getGuarantorName().trim().isEmpty()) {
                throw new RuntimeException("Le nom du garant est obligatoire pour les non-fonctionnaires");
            }
            if (creditRequest.getGuarantorCin() == null || creditRequest.getGuarantorCin().trim().isEmpty()) {
                throw new RuntimeException("Le CIN du garant est obligatoire pour les non-fonctionnaires");
            }
        }
    }
}