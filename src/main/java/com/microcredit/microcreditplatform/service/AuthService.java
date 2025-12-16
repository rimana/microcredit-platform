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

    // Codes secrets
    private static final String ADMIN_SECRET_CODE = "ADMIN_SECRET_2024";
    private static final String AGENT_SECRET_CODE = "AGENT_SECRET_2024"; // Ajouté

    public User registerUser(SignupRequest signUpRequest) {
        System.out.println("🔐 === DEBUG REGISTER START ===");
        System.out.println("📥 Username: " + signUpRequest.getUsername());
        System.out.println("📥 Email: " + signUpRequest.getEmail());
        System.out.println("📥 Role demandé: " + signUpRequest.getRole());

        // Vérifications existantes
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validation du rôle avec code secret
        User.Role finalRole = validateAndDetermineRole(signUpRequest);
        System.out.println("✅ Role final déterminé: " + finalRole);

        // Créer l'utilisateur
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(finalRole);
        user.setPhone(signUpRequest.getPhone());
        user.setCin(signUpRequest.getCin());
        user.setAddress(signUpRequest.getAddress());
        user.setEmployed(signUpRequest.getEmployed());
        user.setMonthlyIncome(signUpRequest.getMonthlyIncome());
        user.setProfession(signUpRequest.getProfession());

        User savedUser = userRepository.save(user);
        System.out.println("✅ User registered with ID: " + savedUser.getId());
        System.out.println("🔐 === DEBUG REGISTER SUCCESS ===");

        return savedUser;
    }

    private User.Role validateAndDetermineRole(SignupRequest signUpRequest) {
        User.Role requestedRole = signUpRequest.getRole();

        // Si aucun rôle spécifié, utiliser CLIENT par défaut
        if (requestedRole == null) {
            return User.Role.CLIENT;
        }

        // Si ADMIN
        if (requestedRole == User.Role.ADMIN) {
            System.out.println("⚠️  Tentative de création ADMIN");
            System.out.println("🔑 Code secret fourni: " + signUpRequest.getAdminSecret());

            if (ADMIN_SECRET_CODE.equals(signUpRequest.getAdminSecret())) {
                System.out.println("✅ Code secret ADMIN valide");
                return User.Role.ADMIN;
            } else {
                System.out.println("❌ Code secret ADMIN invalide");
                throw new RuntimeException("Code secret administrateur invalide");
            }
        }

        // Si AGENT
        if (requestedRole == User.Role.AGENT) {
            System.out.println("👔 Tentative de création AGENT");
            System.out.println("🔑 Code secret fourni: " + signUpRequest.getAdminSecret());

            if (AGENT_SECRET_CODE.equals(signUpRequest.getAdminSecret())) {
                System.out.println("✅ Code secret AGENT valide");
                return User.Role.AGENT;
            } else {
                System.out.println("❌ Code secret AGENT invalide");
                throw new RuntimeException("Code secret agent invalide");
            }
        }

        // Par défaut CLIENT
        return User.Role.CLIENT;
    }
}