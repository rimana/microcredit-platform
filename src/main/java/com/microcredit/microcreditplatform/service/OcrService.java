package com.microcredit.microcreditplatform.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class OcrService {

    public String extractTextFromBase64(String base64Image) {
        try {
            System.out.println("🔍 Début extraction OCR haute qualité...");

            String base64Data = base64Image.contains(",") ?
                    base64Image.split(",")[1] : base64Image;

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            String tessDataPath = ensureTessDataExists();

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);
            tesseract.setLanguage("fra+ara"); // ✅ Français + Arabe
            tesseract.setPageSegMode(6); // Segment de page uniforme
            tesseract.setOcrEngineMode(1);

            // ✅ CORRIGÉ: Utilisation non-dépréciée
            // tesseract.setTessVariable("tessedit_char_whitelist",
            //     "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ./-\\u0600-\\u06FF");

            System.out.println("✅ Configuration Tesseract prête (français + arabe)");

            try (InputStream bis = new ByteArrayInputStream(imageBytes)) {
                BufferedImage image = ImageIO.read(bis);
                if (image == null) {
                    throw new IOException("Format d'image non supporté");
                }

                // ✅ CORRIGÉ: Pré-traitement simplifié et fonctionnel
                BufferedImage processedImage = preprocessImage(image);

                String extractedText = tesseract.doOCR(processedImage);
                System.out.println("✅ Texte extrait avec succès");
                System.out.println("Longueur du texte: " + extractedText.length() + " caractères");

                return extractedText;
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur OCR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la reconnaissance de texte: " + e.getMessage());
        }
    }

    // ✅ CORRIGÉ: Méthode de pré-traitement sans RescaleOp
    private BufferedImage preprocessImage(BufferedImage original) {
        try {
            // Conversion en niveaux de gris pour améliorer l'OCR
            BufferedImage processed = new BufferedImage(
                    original.getWidth(),
                    original.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY
            );

            // Dessiner l'image originale sur l'image en niveaux de gris
            Graphics2D g = processed.createGraphics();
            g.drawImage(original, 0, 0, null);
            g.dispose();

            // ✅ CORRIGÉ: Amélioration du contraste sans RescaleOp
            processed = enhanceContrast(processed);

            System.out.println("✅ Image pré-traitée: " + processed.getWidth() + "x" + processed.getHeight());
            return processed;

        } catch (Exception e) {
            System.err.println("⚠️ Erreur pré-traitement image, utilisation originale: " + e.getMessage());
            return original;
        }
    }

    // ✅ NOUVELLE MÉTHODE: Amélioration du contraste manuelle
    private BufferedImage enhanceContrast(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage enhanced = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Facteurs d'ajustement du contraste
        float contrastFactor = 1.5f;
        int brightnessAdjust = 10;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extraire la composante de gris
                int gray = (rgb >> 16) & 0xFF; // R, G, B ont la même valeur en gris

                // Appliquer le contraste
                int adjusted = (int) ((gray - 128) * contrastFactor + 128 + brightnessAdjust);

                // S'assurer que la valeur reste dans [0, 255]
                adjusted = Math.max(0, Math.min(255, adjusted));

                // Recréer la couleur en niveaux de gris
                int newRgb = (adjusted << 16) | (adjusted << 8) | adjusted;
                enhanced.setRGB(x, y, newRgb);
            }
        }

        return enhanced;
    }

    private String ensureTessDataExists() {
        try {
            Path tessdataPath = Paths.get("src/main/resources/tessdata");
            Path fraFile = tessdataPath.resolve("fra.traineddata");
            Path araFile = tessdataPath.resolve("ara.traineddata");

            // ✅ Vérifier et créer le dossier si nécessaire
            if (!Files.exists(tessdataPath)) {
                Files.createDirectories(tessdataPath);
                System.out.println("✅ Dossier tessdata créé: " + tessdataPath.toAbsolutePath());
            }

            // Télécharger français si manquant
            if (!Files.exists(fraFile)) {
                System.out.println("📥 Téléchargement des données français...");
                downloadTessData("fra", fraFile);
            } else {
                System.out.println("✅ Données français déjà présentes");
            }

            // Télécharger arabe si manquant
            if (!Files.exists(araFile)) {
                System.out.println("📥 Téléchargement des données arabe...");
                downloadTessData("ara", araFile);
            } else {
                System.out.println("✅ Données arabe déjà présentes");
            }

            System.out.println("✅ Données Tesseract français + arabe prêtes");
            return tessdataPath.toAbsolutePath().toString();

        } catch (Exception e) {
            System.err.println("❌ Erreur configuration Tesseract: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossible de configurer Tesseract: " + e.getMessage());
        }
    }

    private void downloadTessData(String lang, Path targetFile) throws IOException {
        try {
            String url = "https://github.com/tesseract-ocr/tessdata/raw/main/" + lang + ".traineddata";

            System.out.println("🌐 Téléchargement depuis: " + url);

            try (InputStream in = new java.net.URL(url).openStream()) {
                Files.copy(in, targetFile);
                System.out.println("✅ Données " + lang + " téléchargées: " + targetFile.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur téléchargement " + lang + ": " + e.getMessage());
            throw new IOException("Impossible de télécharger les données " + lang, e);
        }
    }

    // ✅ MÉTHODE UTILITAIRE: Vérifier l'état des données Tesseract
    public String checkTessDataStatus() {
        try {
            Path tessdataPath = Paths.get("src/main/resources/tessdata");
            Path fraFile = tessdataPath.resolve("fra.traineddata");
            Path araFile = tessdataPath.resolve("ara.traineddata");

            StringBuilder status = new StringBuilder();
            status.append("📊 État des données Tesseract:\n");
            status.append("- Dossier tessdata: ").append(Files.exists(tessdataPath) ? "✅ Présent" : "❌ Absent").append("\n");
            status.append("- Français (fra): ").append(Files.exists(fraFile) ? "✅ Présent" : "❌ Absent").append("\n");
            status.append("- Arabe (ara): ").append(Files.exists(araFile) ? "✅ Présent" : "❌ Absent").append("\n");
            status.append("- Chemin: ").append(tessdataPath.toAbsolutePath());

            return status.toString();
        } catch (Exception e) {
            return "❌ Erreur vérification données: " + e.getMessage();
        }
    }
}