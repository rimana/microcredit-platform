package com.microcredit.microcreditplatform.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;
    private String cin;
    private String address;
    private Boolean employed = false;
    private Double monthlyIncome;
    private String profession;



    public enum Role {
        CLIENT, AGENT, ADMIN
    }



    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

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