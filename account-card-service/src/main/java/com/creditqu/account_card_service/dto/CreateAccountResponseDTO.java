package com.creditqu.account_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponseDTO {
    private Long accountId;
    private String accountNumber;
    private Long cardId;
    private String cardNumber;
    private String cardNumberMasked;
    private Integer expiryMonth;
    private Integer expiryYear;
    private BigDecimal currentLimit;
    private String virtualAccountNumber;
    private String bankCode;
    private String status;
}
