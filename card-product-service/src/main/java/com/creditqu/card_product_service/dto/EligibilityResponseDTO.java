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
public class EligibilityResponseDTO {
    private boolean eligible;
    private String productCode;
    private String productName;
    private String message;
    private BigDecimal suggestedLimit;
}
