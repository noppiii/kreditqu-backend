package com.creditqu.card_product_service.controller;

import com.creditqu.card_product_service.dto.CardProductResponseDTO;
import com.creditqu.card_product_service.dto.EligibilityRequestDTO;
import com.creditqu.card_product_service.dto.EligibilityResponseDTO;
import com.creditqu.card_product_service.service.CardProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CardProductController {

    private final CardProductService cardProductService;

    @GetMapping
    public ResponseEntity<List<CardProductResponseDTO>> getAllActiveProducts() {
        return ResponseEntity.ok(cardProductService.getAllActiveProducts());
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<CardProductResponseDTO> getProductByCode(@PathVariable String productCode) {
        return ResponseEntity.ok(cardProductService.getProductByCode(productCode));
    }

    @PostMapping("/eligibility")
    public ResponseEntity<EligibilityResponseDTO> checkEligibility(@Valid @RequestBody EligibilityRequestDTO request) {
        return ResponseEntity.ok(cardProductService.checkEligibility(request));
    }
}
