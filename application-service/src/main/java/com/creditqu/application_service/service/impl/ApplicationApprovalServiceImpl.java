package com.creditqu.application_service.service.impl;

import com.creditqu.application_service.client.AccountCardServiceClient;
import com.creditqu.application_service.dto.CreateAccountRequestDTO;
import com.creditqu.application_service.dto.CreateAccountResponseDTO;
import com.creditqu.application_service.dto.ScoringResponseDTO;
import com.creditqu.application_service.entity.Application;
import com.creditqu.application_service.repository.ApplicationRepository;
import com.creditqu.application_service.service.ApplicationApprovalService;
import com.creditqu.application_service.service.ApplicationService;
import com.creditqu.common_module.constant.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationApprovalServiceImpl implements ApplicationApprovalService {

    private final ApplicationRepository applicationRepository;
    private final AccountCardServiceClient accountCardServiceClient;
    private final ApplicationService applicationService;

    @Transactional
    @Override
    public void processApproval(Long applicationId, ScoringResponseDTO scoringResponse) {
        log.info("Processing approval for application: {}", applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));

        application.setStatus(ApplicationStatus.APPROVED);
        application.setApprovedLimit(scoringResponse.getLimitRecommendation());
        application.setApprovedAt(java.time.LocalDateTime.now());
        applicationRepository.save(application);

        log.info("Application {} approved with limit: {}",
                application.getApplicationNumber(), scoringResponse.getLimitRecommendation());

        CreateAccountRequestDTO accountRequest = CreateAccountRequestDTO.builder()
                .customerId(application.getCustomerId())
                .applicationId(application.getId())
                .productCode(application.getProductCode())
                .customerName(getCustomerName(application.getCustomerId()))
                .customerEmail(getCustomerEmail(application.getCustomerId()))
                .customerPhone(getCustomerPhone(application.getCustomerId()))
                .approvedLimit(scoringResponse.getLimitRecommendation())
                .build();

        try {
            CreateAccountResponseDTO accountResponse = accountCardServiceClient.createAccountAndCard(accountRequest);
            log.info("Account and card created for application {}: card number: {}",
                    applicationId, accountResponse.getCardNumberMasked());

            application.setNotes("Card issued: " + accountResponse.getCardNumberMasked());
            applicationRepository.save(application);

        } catch (Exception e) {
            log.error("Failed to create account/card for application {}: {}", applicationId, e.getMessage());
            throw new RuntimeException("Account creation failed: " + e.getMessage());
        }
    }

    // TODO: Implement these methods to fetch from customer service
    private String getCustomerName(Long customerId) {
        return "Customer Name";
    }

    private String getCustomerEmail(Long customerId) {
        return "customer@email.com";
    }

    private String getCustomerPhone(Long customerId) {
        return "08123456789";
    }
}
