package com.microcredit.microcreditplatform.dto;

import com.microcredit.microcreditplatform.model.User;

public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
    private String cin;
    private String address;
    private Boolean employed;
    private Double monthlyIncome;
    private String profession;
    private User.Role role = User.Role.CLIENT; // Par défaut CLIENT
    private String adminSecret; // Champ secret pour créer un admin

    // Getters et Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) {
        // Validation du rôle
        if (role != null) {
            this.role = role;
        }
    }

    public String getAdminSecret() { return adminSecret; }
    public void setAdminSecret(String adminSecret) { this.adminSecret = adminSecret; }
}