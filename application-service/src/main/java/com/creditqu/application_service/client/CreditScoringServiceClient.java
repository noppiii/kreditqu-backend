package com.creditqu.application_service.client;

import com.creditqu.application_service.dto.ScoringRequestDTO;
import com.creditqu.application_service.dto.ScoringResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "credit-scoring-service")
public interface CreditScoringServiceClient {

    @PostMapping("/api/scoring/calculate")
    ScoringResponseDTO calculateScore(@RequestBody ScoringRequestDTO request);
}
