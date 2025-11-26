package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.ScanResponse;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class CNIEParser {

    public ScanResponse parseCNIEText(String extractedText) {
        ScanResponse response = new ScanResponse();

        try {
            System.out.println("🔍 Analyse du texte OCR...");

            if (extractedText == null || extractedText.trim().isEmpty()) {
                response.setErrorMessage("Aucun texte extrait de l'image");
                return response;
            }

            // Nettoyer le texte
            String cleanText = extractedText.replace("\r", " ").trim();
            String[] lines = cleanText.split("\n");

            // Analyser chaque ligne
            for (String line : lines) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) continue;

                // Détection CIN (format marocain: 1 lettre + 6 chiffres)
                if (trimmedLine.matches(".*[A-Z]\\d{5,6}.*")) {
                    Pattern cinPattern = Pattern.compile("([A-Z]\\d{5,6})");
                    Matcher cinMatcher = cinPattern.matcher(trimmedLine);
                    if (cinMatcher.find() && response.getCin() == null) {
                        response.setCin(cinMatcher.group(1));
                        System.out.println("✅ CIN trouvé: " + response.getCin());
                    }
                }

                // Détection Date de naissance (JJ/MM/AAAA)
                if (trimmedLine.matches(".*\\d{1,2}/\\d{1,2}/\\d{4}.*")) {
                    Pattern datePattern = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})");
                    Matcher dateMatcher = datePattern.matcher(trimmedLine);
                    if (dateMatcher.find()) {
                        System.out.println("✅ Date de naissance: " + dateMatcher.group(1));
                        // Vous pouvez l'ajouter à la réponse si besoin
                    }
                }

                // Détection Nom (lignes en majuscules raisonnablement longues)
                if (trimmedLine.matches("^[A-ZÉÈÀÇÊÎÔÛÄËÏÖÜ\\s-]{5,30}$")) {
                    if (response.getFullName() == null || trimmedLine.length() > response.getFullName().length()) {
                        response.setFullName(trimmedLine);
                        System.out.println("✅ Nom trouvé: " + response.getFullName());
                    }
                }

                // Détection Adresse (lignes avec texte mixte)
                if (trimmedLine.matches(".*[a-z].*") && trimmedLine.length() > 15) {
                    if (response.getAddress() == null) {
                        response.setAddress(trimmedLine);
                        System.out.println("✅ Adresse trouvée: " + response.getAddress());
                    }
                }
            }

            // Vérifier le succès
            boolean hasData = response.getFullName() != null ||
                    response.getAddress() != null ||
                    response.getCin() != null;

            response.setSuccess(hasData);

            if (!hasData) {
                response.setErrorMessage("Aucune information identifiable trouvée");
                System.out.println("❌ Aucune donnée extraite");
            } else {
                System.out.println("✅ Analyse réussie!");
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur parsing: " + e.getMessage());
            response.setSuccess(false);
            response.setErrorMessage("Erreur d'analyse: " + e.getMessage());
        }

        return response;
    }
}