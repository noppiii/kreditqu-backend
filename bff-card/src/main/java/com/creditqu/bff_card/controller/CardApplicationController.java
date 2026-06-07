package com.creditqu.bff_card.controller;

import com.creditqu.bff_card.dto.*;
import com.creditqu.bff_card.service.CardApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardApplicationController {

    private final CardApplicationService cardApplicationService;

    @PostMapping("/application/submit")
    public ResponseEntity<CreditCardApplicationResponseDTO> submitApplication(
            @Valid @RequestBody CreditCardApplicationRequestDTO request) {
        CreditCardApplicationResponseDTO response = cardApplicationService.submitApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/eligibility/check")
    public ResponseEntity<EligibilityResponseDTO> checkEligibility(
            @Valid @RequestBody EligibilityRequestDTO request) {
        EligibilityResponseDTO response = cardApplicationService.checkEligibility(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAvailableProducts() {
        List<ProductResponseDTO> products = cardApplicationService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }
}
