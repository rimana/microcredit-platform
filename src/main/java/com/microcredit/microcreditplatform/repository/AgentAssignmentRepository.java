// Emplacement: src/main/java/com/microcredit/microcreditplatform/repository/AgentAssignmentRepository.java
package com.microcredit.microcreditplatform.repository;

import com.microcredit.microcreditplatform.model.AgentAssignment;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentAssignmentRepository extends JpaRepository<AgentAssignment, Long> {
    List<AgentAssignment> findByAgent(User agent);
    List<AgentAssignment> findByAgentAndStatus(User agent, String status);
    Optional<AgentAssignment> findByCreditRequestAndStatus(CreditRequest creditRequest, String status);
    List<AgentAssignment> findByStatus(String status);
}