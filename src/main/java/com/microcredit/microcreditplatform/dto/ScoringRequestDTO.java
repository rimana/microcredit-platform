package com.microcredit.microcreditplatform.dto;

public class ScoringRequestDTO {
    private Double monthlyIncome;
    private Double loanAmount;
    private Integer loanDuration;
    private Boolean isFunctionnaire;
    private Boolean employed;
    private Integer age;
    private String profession;
    private Boolean hasGuarantor;
    private String cin;
    private String addressRegion;

    // === GETTERS & SETTERS ===
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }

    public Integer getLoanDuration() { return loanDuration; }
    public void setLoanDuration(Integer loanDuration) { this.loanDuration = loanDuration; }

    public Boolean getIsFunctionnaire() { return isFunctionnaire; }
    public void setIsFunctionnaire(Boolean isFunctionnaire) { this.isFunctionnaire = isFunctionnaire; }

    public Boolean getEmployed() { return employed; }
    public void setEmployed(Boolean employed) { this.employed = employed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public Boolean getHasGuarantor() { return hasGuarantor; }
    public void setHasGuarantor(Boolean hasGuarantor) { this.hasGuarantor = hasGuarantor; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getAddressRegion() { return addressRegion; }
    public void setAddressRegion(String addressRegion) { this.addressRegion = addressRegion; }

    // === CONSTRUCTEURS ===
    public ScoringRequestDTO() {}

    public ScoringRequestDTO(Double monthlyIncome, Double loanAmount, Integer loanDuration,
                             Boolean isFunctionnaire, Boolean employed, Integer age,
                             String profession, Boolean hasGuarantor, String cin,
                             String addressRegion) {
        this.monthlyIncome = monthlyIncome;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.isFunctionnaire = isFunctionnaire;
        this.employed = employed;
        this.age = age;
        this.profession = profession;
        this.hasGuarantor = hasGuarantor;
        this.cin = cin;
        this.addressRegion = addressRegion;
    }

    // === toString() pour débogage ===
    @Override
    public String toString() {
        return "ScoringRequestDTO{" +
                "monthlyIncome=" + monthlyIncome +
                ", loanAmount=" + loanAmount +
                ", loanDuration=" + loanDuration +
                ", isFunctionnaire=" + isFunctionnaire +
                ", employed=" + employed +
                ", age=" + age +
                ", profession='" + profession + '\'' +
                ", hasGuarantor=" + hasGuarantor +
                '}';
    }
}