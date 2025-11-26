package com.microcredit.microcreditplatform.dto;

public class ScanResponse {
    private String fullName;
    private String address;
    private String cin;
    private boolean success;
    private String errorMessage;

    // Constructeurs
    public ScanResponse() {
        this.success = false;
    }

    public ScanResponse(String fullName, String address, String cin, boolean success) {
        this.fullName = fullName;
        this.address = address;
        this.cin = cin;
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
}