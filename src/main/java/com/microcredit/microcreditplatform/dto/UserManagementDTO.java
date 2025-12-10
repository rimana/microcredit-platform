package com.microcredit.microcreditplatform.dto;

import com.microcredit.microcreditplatform.model.User;

import java.time.LocalDateTime;

public class UserManagementDTO {
    private Long id;
    private String username;
    private String email;
    private User.Role role;
    private String phone;
    private String cin;
    private String address;
    private Boolean employed;
    private Double monthlyIncome;
    private String profession;
    private Long creditCount;
    private Double totalBorrowedAmount;
    private LocalDateTime lastLoginDate;

    // Constructeurs
    public UserManagementDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }

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

    public Long getCreditCount() { return creditCount; }
    public void setCreditCount(Long creditCount) { this.creditCount = creditCount; }

    public Double getTotalBorrowedAmount() { return totalBorrowedAmount; }
    public void setTotalBorrowedAmount(Double totalBorrowedAmount) { this.totalBorrowedAmount = totalBorrowedAmount; }

    public LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(LocalDateTime lastLoginDate) { this.lastLoginDate = lastLoginDate; }
}