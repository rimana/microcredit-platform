package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.LoginRequest;
import com.microcredit.microcreditplatform.dto.LoginResponse;
import com.microcredit.microcreditplatform.dto.SignupRequest;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.security.JwtUtils;
import com.microcredit.microcreditplatform.security.UserPrincipal;
import com.microcredit.microcreditplatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔐 === DEBUG LOGIN START ===");
        System.out.println("📥 Username reçu: " + loginRequest.getUsername());
        System.out.println("📥 Password reçu: " + (loginRequest.getPassword() != null ? "***" : "NULL"));

        try {
            System.out.println("🔄 Tentative d'authentification...");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            System.out.println("✅ Authentication successful");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();

            LoginResponse response = new LoginResponse(jwt, user);

            System.out.println("🎉 Login successful for user: " + user.getUsername());
            System.out.println("🔐 === DEBUG LOGIN SUCCESS ===");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ === AUTHENTICATION FAILED ===");
            System.out.println("🚨 Error type: " + e.getClass().getSimpleName());
            System.out.println("🚨 Error message: " + e.getMessage());
            System.out.println("🔍 Stack trace:");
            e.printStackTrace(); // ✅ Affiche la stack trace complète
            System.out.println("🔐 === DEBUG LOGIN FAILED ===");
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest signUpRequest) {
        System.out.println("📝 === DEBUG SIGNUP START ===");
        System.out.println("📥 Signup request for: " + signUpRequest.getUsername());

        try {
            User user = authService.registerUser(signUpRequest);
            System.out.println("✅ User registered: " + user.getUsername());
            System.out.println("📝 === DEBUG SIGNUP SUCCESS ===");
            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
        } catch (RuntimeException e) {
            System.out.println("❌ Signup failed: " + e.getMessage());
            System.out.println("📝 === DEBUG SIGNUP FAILED ===");
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}