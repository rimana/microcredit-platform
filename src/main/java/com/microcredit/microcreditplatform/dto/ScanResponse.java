package com.microcredit.microcreditplatform.dto;

public class ScanResponse {
    private String fullName;
    private String address;
    private String cin;
    private String birthDate;    // ✅ AJOUTÉ
    private String birthPlace;   // ✅ AJOUTÉ
    private boolean success;
    private String errorMessage;

    // Constructeurs
    public ScanResponse() {
        this.success = false;
    }

    public ScanResponse(String fullName, String address, String cin, String birthDate, String birthPlace, boolean success) {
        this.fullName = fullName;
        this.address = address;
        this.cin = cin;
        this.birthDate = birthDate;     // ✅ AJOUTÉ
        this.birthPlace = birthPlace;   // ✅ AJOUTÉ
        this.success = success;
    }

    // Getters et Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    // ✅ AJOUTER ces getters/setters
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // ✅ Méthode utilitaire pour créer une réponse réussie
    public static ScanResponse success(String fullName, String cin, String birthDate, String birthPlace, String address) {
        ScanResponse response = new ScanResponse();
        response.setSuccess(true);
        response.setFullName(fullName);
        response.setCin(cin);
        response.setBirthDate(birthDate);
        response.setBirthPlace(birthPlace);
        response.setAddress(address);
        return response;
    }

    // ✅ Méthode utilitaire pour créer une réponse d'erreur
    public static ScanResponse error(String errorMessage) {
        ScanResponse response = new ScanResponse();
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }
}