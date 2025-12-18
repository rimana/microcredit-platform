package com.microcredit.microcreditplatform.dto;

public class CreditRequestDTO {
    // === CHAMPS DE BASE ===
    private Double amount;
    private Integer duration;
    private Double interestRate;
    private String purpose;
    private Boolean isFunctionnaire;

    // === CHAMPS REQUIS POUR LE SCORING ML ===
    private Double monthlyIncome;
    private Boolean employed;
    private Integer age;
    private String profession;
    private Boolean hasGuarantor;

    // === INFORMATIONS GARANT ===
    private String guarantorName;
    private String guarantorCin;
    private String guarantorPhone;
    private String guarantorAddress;

    // === GETTERS & SETTERS ===

    // Champs de base
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public Boolean getIsFunctionnaire() { return isFunctionnaire; }
    public void setIsFunctionnaire(Boolean isFunctionnaire) { this.isFunctionnaire = isFunctionnaire; }

    // Champs ML
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Boolean getEmployed() { return employed; }
    public void setEmployed(Boolean employed) { this.employed = employed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public Boolean getHasGuarantor() { return hasGuarantor; }
    public void setHasGuarantor(Boolean hasGuarantor) { this.hasGuarantor = hasGuarantor; }

    // Informations garant
    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }

    public String getGuarantorCin() { return guarantorCin; }
    public void setGuarantorCin(String guarantorCin) { this.guarantorCin = guarantorCin; }

    public String getGuarantorPhone() { return guarantorPhone; }
    public void setGuarantorPhone(String guarantorPhone) { this.guarantorPhone = guarantorPhone; }

    public String getGuarantorAddress() { return guarantorAddress; }
    public void setGuarantorAddress(String guarantorAddress) { this.guarantorAddress = guarantorAddress; }

    // === CONSTRUCTEURS ===
    public CreditRequestDTO() {}

    public CreditRequestDTO(Double amount, Integer duration, Double interestRate, String purpose,
                            Boolean isFunctionnaire, Double monthlyIncome, Boolean employed,
                            Integer age, String profession, Boolean hasGuarantor,
                            String guarantorName, String guarantorCin, String guarantorPhone,
                            String guarantorAddress) {
        this.amount = amount;
        this.duration = duration;
        this.interestRate = interestRate;
        this.purpose = purpose;
        this.isFunctionnaire = isFunctionnaire;
        this.monthlyIncome = monthlyIncome;
        this.employed = employed;
        this.age = age;
        this.profession = profession;
        this.hasGuarantor = hasGuarantor;
        this.guarantorName = guarantorName;
        this.guarantorCin = guarantorCin;
        this.guarantorPhone = guarantorPhone;
        this.guarantorAddress = guarantorAddress;
    }

    // === VALIDATION ===
    public boolean isValidForScoring() {
        return amount != null &&
                amount > 0 &&
                duration != null &&
                duration > 0 &&
                monthlyIncome != null &&
                monthlyIncome > 0 &&
                isFunctionnaire != null &&
                employed != null &&
                age != null &&
                age >= 18 &&
                profession != null &&
                !profession.trim().isEmpty() &&
                hasGuarantor != null;
    }

    // === toString() pour débogage ===
    @Override
    public String toString() {
        return "CreditRequestDTO{" +
                "amount=" + amount +
                ", duration=" + duration +
                ", monthlyIncome=" + monthlyIncome +
                ", isFunctionnaire=" + isFunctionnaire +
                ", age=" + age +
                ", profession='" + profession + '\'' +
                '}';
    }
}