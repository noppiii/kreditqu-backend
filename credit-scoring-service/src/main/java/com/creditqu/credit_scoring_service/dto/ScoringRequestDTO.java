package com.creditqu.credit_scoring_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringRequestDTO {
    private Long customerId;
    private Long applicationId;
    private BigDecimal monthlyIncome;
    private Integer age;
    private Integer tenureYears;
    private String employmentType;
    private String nik;
}
