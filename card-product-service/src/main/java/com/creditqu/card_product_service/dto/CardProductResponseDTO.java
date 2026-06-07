package com.creditqu.card_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProductResponseDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal minIncome;
    private Integer minCreditScore;
    private BigDecimal defaultLimit;
    private BigDecimal maxLimit;
    private BigDecimal annualFee;
    private Integer annualFeeGracePeriod;
    private BigDecimal interestRate;
    private BigDecimal latePenaltyRate;
    private BigDecimal cashbackPercentage;
    private BigDecimal rewardsMultiplier;
    private String status;
    private List<String> features;
}