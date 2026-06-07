package com.creditqu.bff_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long customerId;
    private String customerNumber;
    private String fullName;
    private String email;
}
