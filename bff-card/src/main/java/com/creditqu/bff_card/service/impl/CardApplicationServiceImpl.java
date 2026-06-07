package com.creditqu.bff_card.service.impl;

import com.creditqu.bff_card.client.ApplicationServiceClient;
import com.creditqu.bff_card.client.CardProductServiceClient;
import com.creditqu.bff_card.client.CustomerServiceClient;
import com.creditqu.bff_card.client.NotificationServiceClient;
import com.creditqu.bff_card.dto.*;
import com.creditqu.bff_card.service.CardApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardApplicationServiceImpl implements CardApplicationService {

    private final CustomerServiceClient customerServiceClient;
    private final CardProductServiceClient cardProductServiceClient;
    private final ApplicationServiceClient applicationServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public CreditCardApplicationResponseDTO submitApplication(CreditCardApplicationRequestDTO request) {
        log.info("Processing card application for customer: {}", request.getCustomerId());

        CustomerResponseDTO customer = customerServiceClient.getCustomerById(request.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + request.getCustomerId());
        }
        log.info("Customer validated: {}", customer.getFullName());

        EligibilityRequestDTO eligibilityRequest = EligibilityRequestDTO.builder()
                .productCode(request.getProductCode())
                .monthlyIncome(request.getMonthlyIncome())
                .creditScore(null)
                .build();

        EligibilityResponseDTO eligibility = cardProductServiceClient.checkEligibility(eligibilityRequest);
        if (!eligibility.isEligible()) {
            throw new RuntimeException(eligibility.getMessage());
        }
        log.info("Product eligibility check passed: {}", eligibility.getMessage());

        ApplicationRequestDTO appRequest = ApplicationRequestDTO.builder()
                .customerId(request.getCustomerId())
                .productCode(request.getProductCode())
                .desiredLimit(request.getDesiredLimit())
                .monthlyIncome(request.getMonthlyIncome())
                .employmentType(request.getEmploymentType())
                .companyName(request.getCompanyName())
                .tenureYears(request.getTenureYears())
                .notes(request.getNotes())
                .build();

        ApplicationResponseDTO application = applicationServiceClient.submitApplication(appRequest);
        log.info("Application submitted with number: {}", application.getApplicationNumber());

        try {
            notificationServiceClient.sendApplicationReceivedEmail(
                    customer.getEmail(),
                    customer.getFullName(),
                    application.getApplicationNumber()
            );
            log.info("Notification sent to customer: {}", customer.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send notification: {}", e.getMessage());
        }

        return CreditCardApplicationResponseDTO.builder()
                .applicationId(application.getId())
                .applicationNumber(application.getApplicationNumber())
                .status(application.getStatus())
                .message("Pengajuan kartu kredit berhasil diterima. Kami akan memproses dalam 2x24 jam.")
                .submittedAt(application.getSubmittedAt())
                .build();
    }

    @Override
    public EligibilityResponseDTO checkEligibility(EligibilityRequestDTO request) {
        log.info("Checking eligibility for product: {}", request.getProductCode());

        if (request.getProductCode() == null || request.getProductCode().isBlank()) {
            throw new RuntimeException("Product code is required");
        }

        if (request.getMonthlyIncome() == null) {
            throw new RuntimeException("Monthly income is required");
        }

        return cardProductServiceClient.checkEligibility(request);
    }

    @Override
    public List<ProductResponseDTO> getAvailableProducts() {
        log.info("Fetching all available products");
        List<ProductResponseDTO> products = cardProductServiceClient.getAllActiveProducts();

        if (products == null || products.isEmpty()) {
            log.warn("No active products found");
        } else {
            log.info("Found {} active products", products.size());
        }

        return products;
    }
}
