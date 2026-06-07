package com.creditqu.application_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringResponseDTO {
    private Long customerId;
    private Long applicationId;
    private Integer scoreValue;
    private String rating;
    private Integer internalScore;
    private Integer externalScore;
    private String approvalRecommendation;
    private BigDecimal limitRecommendation;
    private LocalDate scoringDate;
    private String message;
}