package com.microcredit.microcreditplatform.dto;

public class ScanRequest {
    private String imageBase64;
    private String cardType;

    // Constructeurs
    public ScanRequest() {}

    public ScanRequest(String imageBase64, String cardType) {
        this.imageBase64 = imageBase64;
        this.cardType = cardType;
    }

    // Getters et Setters
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}