package com.creditqu.bff_card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal minIncome;
    private Integer minCreditScore;
    private BigDecimal defaultLimit;
    private BigDecimal maxLimit;
    private BigDecimal annualFee;
    private BigDecimal interestRate;
}
