package com.creditqu.credit_scoring_service.dto;

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
public class CustomerDataDTO {
    private Long id;
    private String fullName;
    private String nik;
    private LocalDate dateOfBirth;
    private BigDecimal monthlyIncome;
    private Integer tenureYears;
    private String employmentType;
    private Integer age;
}