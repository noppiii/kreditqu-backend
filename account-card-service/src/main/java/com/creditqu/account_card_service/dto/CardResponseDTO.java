package com.creditqu.account_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {
    private Long id;
    private String cardNumber;
    private String cardNumberMasked;
    private String cardholderName;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String status;
    private Boolean isPrimary;
    private LocalDateTime issuedAt;
    private LocalDateTime activatedAt;
}

