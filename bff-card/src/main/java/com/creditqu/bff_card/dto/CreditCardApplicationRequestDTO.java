package com.creditqu.bff_card.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardApplicationRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotNull(message = "Desired limit is required")
    @Min(value = 1000000, message = "Desired limit minimal Rp 1.000.000")
    private BigDecimal desiredLimit;

    @NotNull(message = "Monthly income is required")
    private BigDecimal monthlyIncome;

    private String employmentType;
    private String companyName;
    private Integer tenureYears;
    private String notes;
}
