// UserProfileDTO.java
package com.microcredit.microcreditplatform.dto;

public class UserProfileDTO {
    private String username;
    private String email;
    private String phone;
    private String cin;
    private String address;
    private Boolean employed;
    private Double monthlyIncome;
    private String profession;

    // Constructeurs
    public UserProfileDTO() {}

    public UserProfileDTO(String username, String email, String phone, String cin,
                          String address, Boolean employed, Double monthlyIncome, String profession) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.cin = cin;
        this.address = address;
        this.employed = employed;
        this.monthlyIncome = monthlyIncome;
        this.profession = profession;
    }

    // Getters et Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Boolean getEmployed() { return employed; }
    public void setEmployed(Boolean employed) { this.employed = employed; }

    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
}