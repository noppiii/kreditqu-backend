package com.creditqu.credit_scoring_service.service;

import com.creditqu.credit_scoring_service.dto.ScoringRequestDTO;
import com.creditqu.credit_scoring_service.dto.ScoringResponseDTO;

public interface CreditScoringService {

    ScoringResponseDTO calculateScore(ScoringRequestDTO request);
    ScoringResponseDTO getScoreByApplicationId(Long applicationId);

}
