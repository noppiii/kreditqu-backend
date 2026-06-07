package com.creditqu.bff_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    private Long customerId;
    private String customerNumber;
    private String email;
    private String message;
}
