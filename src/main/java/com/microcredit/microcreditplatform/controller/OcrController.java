package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.ScanRequest;
import com.microcredit.microcreditplatform.dto.ScanResponse;
import com.microcredit.microcreditplatform.service.CNIEParser;
import com.microcredit.microcreditplatform.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private CNIEParser cnieParser;

    @PostMapping("/scan-cnie")
    public ResponseEntity<ScanResponse> scanCNIE(@RequestBody ScanRequest request) {
        try {
            System.out.println("📨 Requête de scan CNIE reçue");

            // Utiliser les getters
            if (request.getImageBase64() == null || request.getImageBase64().trim().isEmpty()) {
                ScanResponse error = new ScanResponse();
                error.setSuccess(false);
                error.setErrorMessage("Image base64 manquante");
                return ResponseEntity.badRequest().body(error);
            }

            String extractedText = ocrService.extractTextFromBase64(request.getImageBase64());
            ScanResponse response = cnieParser.parseCNIEText(extractedText);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("💥 Erreur lors du scan: " + e.getMessage());
            e.printStackTrace();

            ScanResponse errorResponse = new ScanResponse();
            errorResponse.setSuccess(false);
            errorResponse.setErrorMessage("Erreur interne du serveur: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OCR Service is operational - " + System.currentTimeMillis());
    }

    @GetMapping("/test")
    public ResponseEntity<ScanResponse> testEndpoint() {
        ScanResponse testResponse = new ScanResponse();
        testResponse.setFullName("Test User");
        testResponse.setAddress("123 Test Address, Casablanca");
        testResponse.setCin("AB123456");
        testResponse.setSuccess(true);

        return ResponseEntity.ok(testResponse);
    }
}