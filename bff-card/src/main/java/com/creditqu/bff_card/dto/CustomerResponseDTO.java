package com.creditqu.bff_card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String customerNumber;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
}