// Emplacement: src/main/java/com/microcredit/microcreditplatform/repository/CreditDecisionRepository.java
package com.microcredit.microcreditplatform.repository;

import com.microcredit.microcreditplatform.model.CreditDecision;
import com.microcredit.microcreditplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditDecisionRepository extends JpaRepository<CreditDecision, Long> {
    List<CreditDecision> findByAgent(User agent);
    List<CreditDecision> findByAgentAndDecisionDateAfter(User agent, LocalDateTime date);
    List<CreditDecision> findByCreditRequestId(Long creditRequestId);
}