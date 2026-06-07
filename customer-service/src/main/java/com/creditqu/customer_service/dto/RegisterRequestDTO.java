package com.creditqu.customer_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be at least 3 characters")
    private String fullName;

    @NotBlank(message = "National ID number is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "National ID number must be 16 digits")
    private String nik;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Place of birth is required")
    private String placeOfBirth;

    @NotNull(message = "Gender is required")
    private String gender;
}
