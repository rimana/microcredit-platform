package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.SignupRequest;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        user.setPhone(signUpRequest.getPhone());
        user.setCin(signUpRequest.getCin());
        user.setAddress(signUpRequest.getAddress());
        user.setEmployed(signUpRequest.getEmployed());
        user.setMonthlyIncome(signUpRequest.getMonthlyIncome());
        user.setProfession(signUpRequest.getProfession());

        return userRepository.save(user);
    }
}