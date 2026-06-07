package com.creditqu.card_product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityRequestDTO {
    private String productCode;
    private BigDecimal monthlyIncome;
    private Integer creditScore;
}
