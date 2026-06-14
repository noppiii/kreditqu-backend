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
public class VirtualAccountResponseDTO {
    private Long id;
    private String virtualAccountNumber;
    private String bankCode;
    private String status;
    private LocalDateTime expiredAt;
}