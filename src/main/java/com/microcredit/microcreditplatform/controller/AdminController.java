
package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.AdminStatsDTO;
import com.microcredit.microcreditplatform.dto.CreditDetailsDTO;
import com.microcredit.microcreditplatform.dto.MonthlyStatsDTO;
import com.microcredit.microcreditplatform.dto.UserManagementDTO;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.SystemSettings;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==================== GESTION DES UTILISATEURS ====================

    /**
     * Récupérer tous les utilisateurs
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserManagementDTO>> getAllUsers() {
        try {
            List<UserManagementDTO> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer tous les utilisateurs avec pagination
     */
    @GetMapping("/users/paginated")
    public ResponseEntity<Page<UserManagementDTO>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<UserManagementDTO> users = adminService.getAllUsersPaginated(page, size);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les utilisateurs par rôle
     */
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserManagementDTO>> getUsersByRole(@PathVariable User.Role role) {
        try {
            List<UserManagementDTO> users = adminService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Modifier le rôle d'un utilisateur
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        try {
            User.Role newRole = User.Role.valueOf(request.get("role"));
            User updatedUser = adminService.updateUserRole(userId, newRole);
            return ResponseEntity.ok(Map.of(
                "message", "Rôle de l'utilisateur modifié avec succès",
                "user", updatedUser
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rôle invalide"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la modification du rôle"));
        }
    }

    /**
     * Supprimer un utilisateur
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la suppression de l'utilisateur"));
        }
    }

    // ==================== GESTION DES CRÉDITS ====================

    /**
     * Récupérer toutes les demandes de crédit avec détails
     */
    @GetMapping("/credits")
    public ResponseEntity<List<CreditDetailsDTO>> getAllCreditRequests() {
        try {
            List<CreditDetailsDTO> credits = adminService.getAllCreditRequestsWithDetails();
            return ResponseEntity.ok(credits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer toutes les demandes de crédit avec pagination
     */
    @GetMapping("/credits/paginated")
    public ResponseEntity<Page<CreditRequest>> getAllCreditRequestsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CreditRequest> credits = adminService.getAllCreditRequestsPaginated(page, size);
            return ResponseEntity.ok(credits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les demandes de crédit par statut
     */
    @GetMapping("/credits/status/{status}")
    public ResponseEntity<List<CreditRequest>> getCreditsByStatus(@PathVariable CreditRequest.Status status) {
        try {
            List<CreditRequest> credits = adminService.getCreditsByStatus(status);
            return ResponseEntity.ok(credits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Approuver une demande de crédit
     */
    @PutMapping("/credits/{creditId}/approve")
    public ResponseEntity<?> approveCredit(@PathVariable Long creditId) {
        try {
            CreditRequest approvedCredit = adminService.approveCredit(creditId);
            return ResponseEntity.ok(Map.of(
                "message", "Demande de crédit approuvée avec succès",
                "credit", approvedCredit
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de l'approbation de la demande"));
        }
    }

    /**
     * Rejeter une demande de crédit
     */
    @PutMapping("/credits/{creditId}/reject")
    public ResponseEntity<?> rejectCredit(
            @PathVariable Long creditId,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String reason = request != null ? request.get("reason") : "Non spécifié";
            CreditRequest rejectedCredit = adminService.rejectCredit(creditId, reason);
            return ResponseEntity.ok(Map.of(
                "message", "Demande de crédit rejetée",
                "credit", rejectedCredit
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors du rejet de la demande"));
        }
    }

    // ==================== STATISTIQUES ====================

    /**
     * Récupérer les statistiques générales
     */
    @GetMapping("/stats/overview")
    public ResponseEntity<AdminStatsDTO> getOverviewStatistics() {
        try {
            AdminStatsDTO stats = adminService.getOverviewStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les statistiques mensuelles
     */
    @GetMapping("/stats/monthly")
    public ResponseEntity<List<MonthlyStatsDTO>> getMonthlyStatistics(
            @RequestParam(defaultValue = "12") int months) {
        try {
            List<MonthlyStatsDTO> stats = adminService.getMonthlyStatistics(months);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== PARAMÈTRES SYSTÈME ====================

    /**
     * Récupérer les paramètres système
     */
    @GetMapping("/settings")
    public ResponseEntity<SystemSettings> getSystemSettings() {
        try {
            SystemSettings settings = adminService.getSystemSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mettre à jour les paramètres système
     */
    @PutMapping("/settings")
    public ResponseEntity<?> updateSystemSettings(@RequestBody SystemSettings settings) {
        try {
            SystemSettings updatedSettings = adminService.updateSystemSettings(settings);
            return ResponseEntity.ok(Map.of(
                "message", "Paramètres système mis à jour avec succès",
                "settings", updatedSettings
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la mise à jour des paramètres"));
        }
    }

    /**
     * Mettre à jour le taux d'intérêt par défaut
     */
    @PutMapping("/settings/interest-rate")
    public ResponseEntity<?> updateDefaultInterestRate(@RequestBody Map<String, Double> request) {
        try {
            Double newRate = request.get("rate");
            if (newRate == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le taux d'intérêt est requis"));
            }
            adminService.updateDefaultInterestRate(newRate);
            return ResponseEntity.ok(Map.of(
                "message", "Taux d'intérêt mis à jour avec succès",
                "rate", newRate
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la mise à jour du taux d'intérêt"));
        }
    }

    /**
     * Mettre à jour le montant maximum de prêt
     */
    @PutMapping("/settings/max-loan-amount")
    public ResponseEntity<?> updateMaxLoanAmount(@RequestBody Map<String, Double> request) {
        try {
            Double maxAmount = request.get("amount");
            if (maxAmount == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le montant maximum est requis"));
            }
            adminService.updateMaxLoanAmount(maxAmount);
            return ResponseEntity.ok(Map.of(
                "message", "Montant maximum mis à jour avec succès",
                "amount", maxAmount
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la mise à jour du montant maximum"));
        }
    }

    // ==================== ENDPOINT DE SANTÉ ====================

    /**
     * Vérifier que le contrôleur admin est accessible
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "Admin Controller",
            "timestamp", System.currentTimeMillis()
        ));
    }
}

