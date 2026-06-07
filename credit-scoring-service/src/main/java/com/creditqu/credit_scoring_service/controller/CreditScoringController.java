package com.creditqu.credit_scoring_service.controller;

import com.creditqu.credit_scoring_service.dto.ScoringRequestDTO;
import com.creditqu.credit_scoring_service.dto.ScoringResponseDTO;
import com.creditqu.credit_scoring_service.service.CreditScoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring")
@RequiredArgsConstructor
public class CreditScoringController {

    private final CreditScoringService creditScoringService;

    @PostMapping("/calculate")
    public ResponseEntity<ScoringResponseDTO> calculateScore(@Valid @RequestBody ScoringRequestDTO request) {
        ScoringResponseDTO response = creditScoringService.calculateScore(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ScoringResponseDTO> getScoreByApplicationId(@PathVariable Long applicationId) {
        ScoringResponseDTO response = creditScoringService.getScoreByApplicationId(applicationId);
        return ResponseEntity.ok(response);
    }
}
