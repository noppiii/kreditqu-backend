package com.creditqu.application_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDTO {
    private Long customerId;
    private Long applicationId;
    private String productCode;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private BigDecimal approvedLimit;
}