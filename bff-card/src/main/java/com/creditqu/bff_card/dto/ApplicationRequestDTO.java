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
public class ApplicationRequestDTO {
    private Long customerId;
    private String productCode;
    private BigDecimal desiredLimit;
    private BigDecimal monthlyIncome;
    private String employmentType;
    private String companyName;
    private Integer tenureYears;
    private String notes;
}
