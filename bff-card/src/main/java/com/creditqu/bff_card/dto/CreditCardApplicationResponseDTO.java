package com.creditqu.bff_card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardApplicationResponseDTO {
    private Long applicationId;
    private String applicationNumber;
    private String status;
    private String message;
    private LocalDateTime submittedAt;
}
