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
    List<CreditRequest> findByUser(User user);
    List<CreditRequest> findByStatus(CreditRequest.Status status);

    // Compter le nombre de demandes par statut
    long countByStatus(CreditRequest.Status status);

    // Compter le nombre de demandes par utilisateur
    long countByUser(User user);

    // Récupérer les demandes d'un utilisateur par statut
    List<CreditRequest> findByUserAndStatus(User user, CreditRequest.Status status);

    // Somme des montants par statut (retourne 0.0 si aucun)
    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM CreditRequest c WHERE c.status = :status")
    Double sumAmountByStatus(@Param("status") CreditRequest.Status status);

    // Récupérer toutes les demandes en joignant les utilisateurs pour éviter les lazy loads
    @Query("SELECT DISTINCT c FROM CreditRequest c JOIN FETCH c.user")
    List<CreditRequest> findAllWithUsers();
}