package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.CreditRequestDTO;
import com.microcredit.microcreditplatform.dto.ScoringRequestDTO;
import com.microcredit.microcreditplatform.dto.ScoringResponseDTO;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class XGBoostMLService {

    private static final Logger logger = LoggerFactory.getLogger(XGBoostMLService.class);

    @PostConstruct
    public void init() {
        logger.info("✅ Service XGBoostMLService initialisé");
    }

    // ✅ MÉTHODE POUR ACCEPTER CreditRequestDTO
    public ScoringResponseDTO calculateScore(CreditRequestDTO creditRequestDTO) {
        // Convertir en ScoringRequestDTO
        ScoringRequestDTO request = new ScoringRequestDTO();
        request.setMonthlyIncome(creditRequestDTO.getMonthlyIncome());
        request.setLoanAmount(creditRequestDTO.getAmount());
        request.setLoanDuration(creditRequestDTO.getDuration());
        request.setIsFunctionnaire(creditRequestDTO.getIsFunctionnaire());
        request.setEmployed(creditRequestDTO.getEmployed());
        request.setAge(creditRequestDTO.getAge());
        request.setProfession(creditRequestDTO.getProfession());
        request.setHasGuarantor(creditRequestDTO.getHasGuarantor());

        return calculateScore(request);
    }

    // ✅ MÉTHODE PRINCIPALE DE CALCUL
    public ScoringResponseDTO calculateScore(ScoringRequestDTO request) {
        logger.info("🧮 Calcul du score ML pour: revenu={}, montant={}, durée={}",
                request.getMonthlyIncome(), request.getLoanAmount(), request.getLoanDuration());

        ScoringResponseDTO response = new ScoringResponseDTO();

        int baseScore = calculateBasicScore(request);
        double proba = calculateProbability(baseScore, request);
        String riskLevel = determineRiskLevel(baseScore, proba);
        String recommendation = generateRecommendation(baseScore, request);
        List<String> redFlags = findRedFlags(request);
        List<String> positives = findPositiveFactors(request);
        double maxAmount = calculateMaxAmount(request, baseScore);

        response.setScore(baseScore);
        response.setRiskLevel(riskLevel);
        response.setProbabilityDefault(proba);
        response.setRecommendation(recommendation);
        response.setRedFlags(redFlags);
        response.setPositiveFactors(positives);
        response.setMaxRecommendedAmount(maxAmount);
        response.setSuggestedDuration(request.getLoanDuration());

        logger.info("✅ Score calculé: {} ({}), Recommandation: {}",
                baseScore, riskLevel, recommendation);

        return response;
    }

    private int calculateBasicScore(ScoringRequestDTO request) {
        int score = 650;

        if (request.getMonthlyIncome() != null) {
            if (request.getMonthlyIncome() > 15000) score += 60;
            else if (request.getMonthlyIncome() > 10000) score += 40;
            else if (request.getMonthlyIncome() > 7000) score += 25;
            else if (request.getMonthlyIncome() > 5000) score += 10;
            else if (request.getMonthlyIncome() < 3000) score -= 30;
        }

        if (request.getMonthlyIncome() != null && request.getLoanAmount() != null) {
            double ratio = request.getLoanAmount() / request.getMonthlyIncome();
            if (ratio > 12) score -= 100;
            else if (ratio > 8) score -= 50;
            else if (ratio < 3) score += 20;
        }

        if (Boolean.TRUE.equals(request.getIsFunctionnaire())) {
            score += 40;
        }

        if (Boolean.TRUE.equals(request.getEmployed())) {
            score += 30;
        }

        if (Boolean.TRUE.equals(request.getHasGuarantor())) {
            score += 25;
        }

        if (request.getLoanDuration() != null) {
            if (request.getLoanDuration() > 36) score -= 20;
            else if (request.getLoanDuration() < 12) score += 10;
        }

        if (request.getAge() != null) {
            if (request.getAge() >= 30 && request.getAge() <= 50) score += 30;
            else if (request.getAge() < 25 || request.getAge() > 60) score -= 20;
        }

        return Math.min(850, Math.max(300, score));
    }

    private double calculateProbability(int score, ScoringRequestDTO request) {
        double baseProba = (850 - score) / 550.0;

        if (Boolean.TRUE.equals(request.getIsFunctionnaire())) baseProba *= 0.7;
        if (Boolean.TRUE.equals(request.getHasGuarantor())) baseProba *= 0.8;
        if (request.getMonthlyIncome() != null && request.getMonthlyIncome() > 8000) baseProba *= 0.6;

        return Math.min(0.99, Math.max(0.01, baseProba));
    }

    private String determineRiskLevel(int score, double proba) {
        if (score >= 750 || proba < 0.05) return "FAIBLE";
        if (score >= 600 || proba < 0.15) return "MOYEN";
        return "ÉLEVÉ";
    }

    private String generateRecommendation(int score, ScoringRequestDTO request) {
        if (score >= 700) return "APPROUVER";
        if (score >= 600) return "APPROUVER_AVEC_CONDITIONS";
        if (score >= 500) return "REVOIR_MONTANT";
        return "REJETER";
    }

    private List<String> findRedFlags(ScoringRequestDTO request) {
        List<String> flags = new ArrayList<>();

        if (request.getMonthlyIncome() != null && request.getLoanAmount() != null) {
            double ratio = request.getLoanAmount() / request.getMonthlyIncome();
            if (ratio > 10) flags.add("Ratio dette/revenu élevé (" + String.format("%.1f", ratio) + ")");
        }

        if (request.getMonthlyIncome() != null && request.getMonthlyIncome() < 2000) {
            flags.add("Revenu mensuel insuffisant");
        }

        if (Boolean.FALSE.equals(request.getEmployed()) &&
                Boolean.FALSE.equals(request.getIsFunctionnaire())) {
            flags.add("Pas de statut d'emploi stable");
        }

        if (request.getLoanDuration() != null && request.getLoanDuration() > 48) {
            flags.add("Durée de prêt trop longue");
        }

        if (request.getAge() != null && request.getAge() < 23) {
            flags.add("Âge trop jeune");
        }

        return flags;
    }

    private List<String> findPositiveFactors(ScoringRequestDTO request) {
        List<String> positives = new ArrayList<>();

        if (Boolean.TRUE.equals(request.getIsFunctionnaire())) {
            positives.add("Fonctionnaire (stabilité)");
        }

        if (Boolean.TRUE.equals(request.getHasGuarantor())) {
            positives.add("Présence d'un garant");
        }

        if (request.getMonthlyIncome() != null && request.getMonthlyIncome() > 8000) {
            positives.add("Revenu confortable");
        }

        if (request.getAge() != null && request.getAge() >= 30 && request.getAge() <= 50) {
            positives.add("Âge optimal (stabilité professionnelle)");
        }

        if (request.getProfession() != null) {
            String profession = request.getProfession().toLowerCase();
            if (profession.contains("médecin") || profession.contains("ingénieur") ||
                    profession.contains("avocat") || profession.contains("cadre")) {
                positives.add("Profession prestigieuse");
            }
        }

        return positives;
    }

    private double calculateMaxAmount(ScoringRequestDTO request, int score) {
        if (request.getMonthlyIncome() == null) return 10000.0;

        double base = request.getMonthlyIncome() * 6;

        if (score >= 750) base *= 1.5;
        else if (score >= 650) base *= 1.2;
        else if (score < 550) base *= 0.7;

        return Math.min(base, 50000.0);
    }
}