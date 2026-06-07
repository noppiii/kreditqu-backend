package com.creditqu.customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String customerNumber;
    private String fullName;
    private String nik;
    private String email;
    private String phoneNumber;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String gender;
    private Boolean isActive;
    private LocalDateTime createdAt;
}