package com.microcredit.microcreditplatform.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
            System.out.println("🔍 Début extraction OCR...");

            // Nettoyer le base64
            String base64Data = base64Image.contains(",") ?
                    base64Image.split(",")[1] : base64Image;

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // S'assurer que les données Tesseract existent
            String tessDataPath = ensureTessDataExists();

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);
            tesseract.setLanguage("fra");
            tesseract.setPageSegMode(6);
            tesseract.setOcrEngineMode(1);

            System.out.println("✅ Configuration Tesseract prête");

            // Convertir bytes en BufferedImage
            try (InputStream bis = new ByteArrayInputStream(imageBytes)) {
                BufferedImage image = ImageIO.read(bis);
                if (image == null) {
                    throw new IOException("Format d'image non supporté");
                }

                String extractedText = tesseract.doOCR(image);
                System.out.println("✅ Texte extrait avec succès");
                return extractedText;
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur OCR: " + e.getMessage());
            throw new RuntimeException("Erreur lors de la reconnaissance de texte: " + e.getMessage());
        }
    }

    private String ensureTessDataExists() {
        try {
            // Chemin dans les ressources
            Path tessdataPath = Paths.get("src/main/resources/tessdata");
            Path fraFile = tessdataPath.resolve("fra.traineddata");

            // Si le fichier n'existe pas, le télécharger
            if (!Files.exists(fraFile)) {
                System.out.println("📥 Téléchargement des données Tesseract...");
                Files.createDirectories(tessdataPath);

                String url = "https://github.com/tesseract-ocr/tessdata/raw/main/fra.traineddata";

                try (InputStream in = new java.net.URL(url).openStream()) {
                    Files.copy(in, fraFile);
                    System.out.println("✅ Données Tesseract téléchargées: " + fraFile.toAbsolutePath());
                }
            } else {
                System.out.println("✅ Données Tesseract déjà présentes: " + fraFile.toAbsolutePath());
            }

            return tessdataPath.toAbsolutePath().toString();

        } catch (Exception e) {
            System.err.println("❌ Erreur configuration Tesseract: " + e.getMessage());
            throw new RuntimeException("Impossible de configurer Tesseract: " + e.getMessage());
        }
    }
}