// UserProfileController.java
package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.UserProfileDTO;
import com.microcredit.microcreditplatform.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileDTO profile = userProfileService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateProfile(
            @RequestBody UserProfileDTO profileDTO,
            Authentication authentication) {
        String username = authentication.getName();
        UserProfileDTO updatedProfile = userProfileService.updateProfile(username, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}