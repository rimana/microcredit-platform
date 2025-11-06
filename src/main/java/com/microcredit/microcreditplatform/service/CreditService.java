package com.microcredit.microcreditplatform.service;

import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.model.User;
import com.microcredit.microcreditplatform.repository.CreditRequestRepository;
import com.microcredit.microcreditplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditService {

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public CreditRequest createCreditRequest(CreditRequest creditRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validation métier
        if (creditRequest.getAmount() == null || creditRequest.getAmount() <= 0) {
            throw new RuntimeException("Le montant doit être positif");
        }

        if (creditRequest.getDuration() == null || creditRequest.getDuration() <= 0) {
            throw new RuntimeException("La durée doit être positive");
        }

        // Validation pour les non-fonctionnaires
        if (creditRequest.getIsFunctionnaire() != null && !creditRequest.getIsFunctionnaire()) {
            if (creditRequest.getGuarantorName() == null || creditRequest.getGuarantorName().trim().isEmpty()) {
                throw new RuntimeException("Le nom du garant est obligatoire pour les non-fonctionnaires");
            }
            if (creditRequest.getGuarantorCin() == null || creditRequest.getGuarantorCin().trim().isEmpty()) {
                throw new RuntimeException("Le CIN du garant est obligatoire pour les non-fonctionnaires");
            }
        }

        creditRequest.setUser(user);
        return creditRequestRepository.save(creditRequest);
    }

    public List<CreditRequest> getUserCreditRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return creditRequestRepository.findByUser(user);
    }

    public List<CreditRequest> getAllCreditRequests() {
        return creditRequestRepository.findAll();
    }

    public Optional<CreditRequest> getCreditRequestById(Long id) {
        return creditRequestRepository.findById(id);
    }

    public CreditRequest updateCreditRequestStatus(Long id, CreditRequest.Status status) {
        CreditRequest creditRequest = creditRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        creditRequest.setStatus(status);
        return creditRequestRepository.save(creditRequest);
    }
}