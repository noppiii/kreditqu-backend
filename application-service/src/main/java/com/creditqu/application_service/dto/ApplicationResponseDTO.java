package com.creditqu.application_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {
    private Long id;
    private String applicationNumber;
    private Long customerId;
    private String productCode;
    private BigDecimal desiredLimit;
    private String status;
    private String rejectionReason;
    private BigDecimal approvedLimit;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createdAt;
}
