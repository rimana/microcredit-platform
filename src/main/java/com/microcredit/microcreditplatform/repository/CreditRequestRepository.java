package com.microcredit.microcreditplatform.repository;

import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {

    // ==================== MÉTHODES EXISTANTES ====================
    List<CreditRequest> findByUser(User user);
    List<CreditRequest> findByStatus(CreditRequest.Status status);
    long countByStatus(CreditRequest.Status status);
    long countByUser(User user);
    List<CreditRequest> findByUserAndStatus(User user, CreditRequest.Status status);

    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM CreditRequest c WHERE c.status = :status")
    Double sumAmountByStatus(@Param("status") CreditRequest.Status status);

    @Query("SELECT DISTINCT c FROM CreditRequest c JOIN FETCH c.user")
    List<CreditRequest> findAllWithUsers();

    // ==================== MÉTHODES AJOUTÉES POUR CORRECTION ====================
    List<CreditRequest> findByUserOrderByCreatedAtDesc(User user);
    List<CreditRequest> findAllByOrderByCreatedAtDesc();
    List<CreditRequest> findByStatusOrderByCreatedAtDesc(CreditRequest.Status status);

    // ==================== NOUVELLES MÉTHODES POUR DASHBOARD ====================

    // ✅ Pour récupérer les demandes EN ATTENTE AVEC SCORING (dashboard)
    @Query("SELECT c FROM CreditRequest c WHERE c.status = 'PENDING' AND c.score IS NOT NULL ORDER BY c.createdAt DESC")
    List<CreditRequest> findPendingWithScoring();

    // ✅ Pour récupérer TOUTES les demandes avec scoring
    @Query("SELECT c FROM CreditRequest c WHERE c.score IS NOT NULL ORDER BY c.createdAt DESC")
    List<CreditRequest> findAllWithScoring();

    // ✅ Pour récupérer les demandes par niveau de risque
    @Query("SELECT c FROM CreditRequest c WHERE c.riskLevel = :riskLevel ORDER BY c.createdAt DESC")
    List<CreditRequest> findByRiskLevel(@Param("riskLevel") String riskLevel);

    // ✅ Pour statistiques du dashboard
    @Query("SELECT COUNT(c) as total, " +
            "AVG(c.score) as avgScore, " +
            "MIN(c.score) as minScore, " +
            "MAX(c.score) as maxScore " +
            "FROM CreditRequest c WHERE c.score IS NOT NULL")
    Object[] getDashboardStats();
}