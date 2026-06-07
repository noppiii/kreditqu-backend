package com.creditqu.bff_auth.service;

import com.creditqu.bff_auth.client.CustomerServiceClient;
import com.creditqu.bff_auth.client.NotificationServiceClient;
import com.creditqu.bff_auth.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CustomerServiceClient customerServiceClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final NotificationServiceClient notificationServiceClient;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        log.info("Processing registration for email: {}", request.getEmail());

        Boolean emailExists = customerServiceClient.existsByEmail(request.getEmail());
        if (emailExists) {
            throw new RuntimeException("Email already registered");
        }

        CustomerResponseDTO customer = customerServiceClient.register(request);
        log.info("Customer registered with ID: {}", customer.getId());
        notificationServiceClient.sendWelcomeEmail(customer.getId(), customer.getEmail(), customer.getFullName());

        return RegisterResponseDTO.builder()
                .customerId(customer.getId())
                .customerNumber(customer.getCustomerNumber())
                .email(customer.getEmail())
                .message("Registrasi berhasil, silahkan login")
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Processing login for email: {}", request.getEmail());

        CustomerResponseDTO customer = customerServiceClient.getCustomerByEmail(request.getEmail());

        // TODO: verify password

        String accessToken = jwtService.generateToken(customer.getId(), customer.getEmail());
        String refreshToken = jwtService.generateToken(customer.getId(), customer.getEmail());

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600000L)
                .customerId(customer.getId())
                .customerNumber(customer.getCustomerNumber())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .build();
    }
}
