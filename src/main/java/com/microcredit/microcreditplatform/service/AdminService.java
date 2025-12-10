package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.dto.AdminStatsDTO;
import com.microcredit.microcreditplatform.dto.CreditDetailsDTO;
import com.microcredit.microcreditplatform.dto.MonthlyStatsDTO;
import com.microcredit.microcreditplatform.dto.UserManagementDTO;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.SystemSettings;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.repository.CreditRequestRepository;
import com.microcredit.microcreditplatform.repository.SystemSettingsRepository;
import com.microcredit.microcreditplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    // ==================== GESTION DES UTILISATEURS ====================
    
    public List<UserManagementDTO> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::convertToUserManagementDTO)
                .collect(Collectors.toList());
    }

    public Page<UserManagementDTO> getAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return userRepository.findAll(pageable)
                .map(this::convertToUserManagementDTO);
    }

    public List<UserManagementDTO> getUsersByRole(User.Role role) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == role)
                .map(this::convertToUserManagementDTO)
                .collect(Collectors.toList());
    }

    public User updateUserRole(Long userId, User.Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        
        // Validation: Ne pas permettre de changer le dernier admin
        if (user.getRole() == User.Role.ADMIN && newRole != User.Role.ADMIN) {
            long adminCount = userRepository.countByRole(User.Role.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException("Impossible de modifier le dernier administrateur");
            }
        }
        
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        
        // Vérifications avant suppression
        if (user.getRole() == User.Role.ADMIN) {
            long adminCount = userRepository.countByRole(User.Role.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException("Impossible de supprimer le dernier administrateur");
            }
        }
        
        // Vérifier s'il a des crédits actifs
        List<CreditRequest> activeCredits = creditRequestRepository.findByUserAndStatus(
            user, CreditRequest.Status.APPROVED
        );
        
        if (!activeCredits.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un utilisateur avec des crédits actifs");
        }
        
        userRepository.delete(user);
    }

    // ==================== GESTION DES CRÉDITS ====================
    
    public List<CreditDetailsDTO> getAllCreditRequestsWithDetails() {
        return creditRequestRepository.findAllWithUsers()
                .stream()
                .map(this::convertToCreditDetailsDTO)
                .collect(Collectors.toList());
    }

    public Page<CreditRequest> getAllCreditRequestsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return creditRequestRepository.findAll(pageable);
    }

    public List<CreditRequest> getCreditsByStatus(CreditRequest.Status status) {
        return creditRequestRepository.findByStatus(status);
    }

    public CreditRequest approveCredit(Long creditId) {
        CreditRequest credit = creditRequestRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Demande de crédit non trouvée avec l'ID: " + creditId));
        
        if (credit.getStatus() != CreditRequest.Status.PENDING) {
            throw new RuntimeException("Seules les demandes en attente peuvent être approuvées");
        }
        
        credit.setStatus(CreditRequest.Status.APPROVED);
        return creditRequestRepository.save(credit);
    }

    public CreditRequest rejectCredit(Long creditId, String reason) {
        CreditRequest credit = creditRequestRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Demande de crédit non trouvée avec l'ID: " + creditId));
        
        if (credit.getStatus() != CreditRequest.Status.PENDING) {
            throw new RuntimeException("Seules les demandes en attente peuvent être rejetées");
        }
        
        credit.setStatus(CreditRequest.Status.REJECTED);
        // Note: Si vous avez un champ rejectionReason dans CreditRequest, vous pouvez l'utiliser ici
        // credit.setRejectionReason(reason);
        return creditRequestRepository.save(credit);
    }

    // ==================== STATISTIQUES ====================
    
    public AdminStatsDTO getOverviewStatistics() {
        AdminStatsDTO stats = new AdminStatsDTO();
        
        // Statistiques utilisateurs
        stats.setTotalUsers(userRepository.count());
        stats.setActiveClients(userRepository.countByRole(User.Role.CLIENT));
        stats.setTotalAgents(userRepository.countByRole(User.Role.AGENT));
        stats.setTotalAdmins(userRepository.countByRole(User.Role.ADMIN));
        
        // Statistiques crédits
        stats.setTotalCredits(creditRequestRepository.count());
        stats.setPendingCredits(creditRequestRepository.countByStatus(CreditRequest.Status.PENDING));
        stats.setApprovedCredits(creditRequestRepository.countByStatus(CreditRequest.Status.APPROVED));
        stats.setRejectedCredits(creditRequestRepository.countByStatus(CreditRequest.Status.REJECTED));
        
        // Statistiques financières
        stats.setTotalAmount(creditRequestRepository.sumAmountByStatus(CreditRequest.Status.APPROVED));
        stats.setPendingAmount(creditRequestRepository.sumAmountByStatus(CreditRequest.Status.PENDING));
        stats.setApprovedAmount(creditRequestRepository.sumAmountByStatus(CreditRequest.Status.APPROVED));
        
        // Calcul du montant moyen
        if (stats.getTotalCredits() > 0) {
            stats.setAverageLoanAmount(stats.getTotalAmount() / stats.getTotalCredits());
        } else {
            stats.setAverageLoanAmount(0.0);
        }
        
        // Taux de défaut (à implémenter selon votre logique métier)
        stats.setDefaultRate(0.0);
        
        return stats;
    }

   public List<MonthlyStatsDTO> getMonthlyStatistics(int months) {
    // Version simplifiée qui fonctionne
    LocalDateTime startDate = LocalDateTime.now().minusMonths(months);
    
    List<CreditRequest> allCredits = creditRequestRepository.findAll();
    
    // Grouper par mois/année manuellement
    Map<String, MonthlyStatsDTO> monthlyStatsMap = new HashMap<>();
    
    for (CreditRequest credit : allCredits) {
        if (credit.getCreatedAt().isAfter(startDate)) {
            int month = credit.getCreatedAt().getMonthValue();
            int year = credit.getCreatedAt().getYear();
            String key = year + "-" + month;
            
            MonthlyStatsDTO stats = monthlyStatsMap.getOrDefault(key, 
                new MonthlyStatsDTO(month, year, 0L, 0.0));
            
            stats.setCreditCount(stats.getCreditCount() + 1);
            stats.setTotalAmount(stats.getTotalAmount() + (credit.getAmount() != null ? credit.getAmount() : 0.0));
            
            monthlyStatsMap.put(key, stats);
        }
    }
    
    return new ArrayList<>(monthlyStatsMap.values());
}
        
      

    // ==================== PARAMÈTRES SYSTÈME ====================
    
    public SystemSettings getSystemSettings() {
        return systemSettingsRepository.findFirst()
                .orElse(createDefaultSettings());
    }

    public SystemSettings updateSystemSettings(SystemSettings settings) {
        SystemSettings existingSettings = getSystemSettings();
        existingSettings.setDefaultInterestRate(settings.getDefaultInterestRate());
        existingSettings.setMaxLoanAmount(settings.getMaxLoanAmount());
        existingSettings.setMinLoanAmount(settings.getMinLoanAmount());
        existingSettings.setMaxLoanDuration(settings.getMaxLoanDuration());
        existingSettings.setMinLoanDuration(settings.getMinLoanDuration());
        existingSettings.setSystemMaintenance(settings.getSystemMaintenance());
        existingSettings.setMaintenanceMessage(settings.getMaintenanceMessage());
        
        return systemSettingsRepository.save(existingSettings);
    }

    public void updateDefaultInterestRate(Double newRate) {
        if (newRate <= 0 || newRate > 100) {
            throw new RuntimeException("Le taux d'intérêt doit être entre 0 et 100%");
        }
        
        SystemSettings settings = getSystemSettings();
        settings.setDefaultInterestRate(newRate);
        systemSettingsRepository.save(settings);
    }

    public void updateMaxLoanAmount(Double maxAmount) {
        if (maxAmount <= 0) {
            throw new RuntimeException("Le montant maximum doit être positif");
        }
        
        SystemSettings settings = getSystemSettings();
        if (maxAmount < settings.getMinLoanAmount()) {
            throw new RuntimeException("Le montant maximum ne peut pas être inférieur au montant minimum");
        }
        
        settings.setMaxLoanAmount(maxAmount);
        systemSettingsRepository.save(settings);
    }

    // ==================== MÉTHODES UTILITAIRES ====================
    
    private UserManagementDTO convertToUserManagementDTO(User user) {
        UserManagementDTO dto = new UserManagementDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPhone(user.getPhone());
        dto.setCin(user.getCin());
        dto.setAddress(user.getAddress());
        dto.setEmployed(user.getEmployed());
        dto.setMonthlyIncome(user.getMonthlyIncome());
        dto.setProfession(user.getProfession());
        
        // Calculer le nombre de crédits
        dto.setCreditCount(creditRequestRepository.countByUser(user));
        
        // Calculer le montant total emprunté pour cet utilisateur spécifique
        List<CreditRequest> userApprovedCredits = creditRequestRepository.findByUserAndStatus(user, CreditRequest.Status.APPROVED);
        Double totalBorrowed = userApprovedCredits.stream()
                .mapToDouble(credit -> credit.getAmount() != null ? credit.getAmount() : 0.0)
                .sum();
        dto.setTotalBorrowedAmount(totalBorrowed);
        
        return dto;
    }

    private CreditDetailsDTO convertToCreditDetailsDTO(CreditRequest credit) {
        CreditDetailsDTO dto = new CreditDetailsDTO();
        dto.setId(credit.getId());
        dto.setAmount(credit.getAmount());
        dto.setDuration(credit.getDuration());
        dto.setInterestRate(credit.getInterestRate());
        dto.setPurpose(credit.getPurpose());
        dto.setIsFunctionnaire(credit.getIsFunctionnaire());
        dto.setStatus(credit.getStatus());
        dto.setCreatedAt(credit.getCreatedAt());
        
        // Informations utilisateur
        User user = credit.getUser();
        if (user != null) {
            dto.setClientUsername(user.getUsername());
            dto.setClientEmail(user.getEmail());
            dto.setClientPhone(user.getPhone());
            dto.setClientCin(user.getCin());
        }
        
        // Informations garant
        dto.setGuarantorName(credit.getGuarantorName());
        dto.setGuarantorCin(credit.getGuarantorCin());
        dto.setGuarantorPhone(credit.getGuarantorPhone());
        dto.setGuarantorAddress(credit.getGuarantorAddress());
        
        return dto;
    }

    private SystemSettings createDefaultSettings() {
        SystemSettings settings = new SystemSettings();
        settings.setDefaultInterestRate(12.0);
        settings.setMaxLoanAmount(50000.0);
        settings.setMinLoanAmount(1000.0);
        settings.setMaxLoanDuration(60);
        settings.setMinLoanDuration(6);
        settings.setSystemMaintenance(false);
        
        return systemSettingsRepository.save(settings);
    }
}