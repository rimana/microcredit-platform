package com.microcredit.microcreditplatform.repository;

import com.microcredit.microcreditplatform.model.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {
    
    // Méthode Spring Data JPA automatique
    List<SystemSettings> findAllByOrderByIdAsc();
    
    // Méthode pour récupérer le premier élément
    default Optional<SystemSettings> findFirst() {
        List<SystemSettings> results = findAllByOrderByIdAsc();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}