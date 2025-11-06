package com.microcredit.microcreditplatform.controller;

import com.microcredit.microcreditplatform.dto.CreditRequestDTO;
import com.microcredit.microcreditplatform.model.CreditRequest;
import com.microcredit.microcreditplatform.security.UserPrincipal;
import com.microcredit.microcreditplatform.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @PostMapping("/request")
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequestDTO creditRequestDTO, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // Convertir DTO en Entity
            CreditRequest creditRequest = new CreditRequest();
            creditRequest.setAmount(creditRequestDTO.getAmount());
            creditRequest.setDuration(creditRequestDTO.getDuration());
            creditRequest.setInterestRate(creditRequestDTO.getInterestRate());
            creditRequest.setPurpose(creditRequestDTO.getPurpose());
            creditRequest.setIsFunctionnaire(creditRequestDTO.getIsFunctionnaire());

            // Si non fonctionnaire, ajouter les infos du garant
            if (creditRequestDTO.getIsFunctionnaire() != null && !creditRequestDTO.getIsFunctionnaire()) {
                creditRequest.setGuarantorName(creditRequestDTO.getGuarantorName());
                creditRequest.setGuarantorCin(creditRequestDTO.getGuarantorCin());
                creditRequest.setGuarantorPhone(creditRequestDTO.getGuarantorPhone());
                creditRequest.setGuarantorAddress(creditRequestDTO.getGuarantorAddress());
            }

            CreditRequest savedRequest = creditService.createCreditRequest(creditRequest, userPrincipal.getUsername());
            return ResponseEntity.ok(savedRequest);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getUserCreditRequests(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<CreditRequest> requests = creditService.getUserCreditRequests(userPrincipal.getUsername());
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllCreditRequests() {
        List<CreditRequest> requests = creditService.getAllCreditRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateCreditRequestStatus(@PathVariable Long id, @RequestParam CreditRequest.Status status) {
        try {
            CreditRequest updatedRequest = creditService.updateCreditRequestStatus(id, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}