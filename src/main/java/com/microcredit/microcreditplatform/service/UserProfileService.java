// UserProfileService.java
package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.UserProfileDTO;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDTO getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));

        return mapToDTO(user);
    }

    public UserProfileDTO updateProfile(String username, UserProfileDTO profileDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));

        // Mise à jour des champs
        user.setEmail(profileDTO.getEmail());
        user.setPhone(profileDTO.getPhone());
        user.setCin(profileDTO.getCin());
        user.setAddress(profileDTO.getAddress());
        user.setEmployed(profileDTO.getEmployed());
        user.setMonthlyIncome(profileDTO.getMonthlyIncome());
        user.setProfession(profileDTO.getProfession());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    private UserProfileDTO mapToDTO(User user) {
        return new UserProfileDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getCin(),
                user.getAddress(),
                user.getEmployed(),
                user.getMonthlyIncome(),
                user.getProfession()
        );
    }
}