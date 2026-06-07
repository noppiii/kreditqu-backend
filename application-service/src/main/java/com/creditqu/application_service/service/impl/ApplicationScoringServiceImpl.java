package com.creditqu.application_service.service.impl;

import com.creditqu.application_service.client.CreditScoringServiceClient;
import com.creditqu.application_service.dto.ScoringRequestDTO;
import com.creditqu.application_service.dto.ScoringResponseDTO;
import com.creditqu.application_service.entity.Application;
import com.creditqu.application_service.repository.ApplicationRepository;
import com.creditqu.application_service.service.ApplicationScoringService;
import com.creditqu.application_service.service.ApplicationService;
import com.creditqu.common_module.constant.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationScoringServiceImpl implements ApplicationScoringService {

    private final CreditScoringServiceClient scoringServiceClient;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;

    @Async
    @Transactional
    @Override
    public void processScoring(Long applicationId, Long customerId, BigDecimal monthlyIncome,
                               Integer tenureYears, String employmentType, String nik, Integer age) {
        log.info("Processing scoring for application: {}", applicationId);

        try {
            applicationService.updateApplicationStatus(applicationId, ApplicationStatus.VERIFICATION, "Processing credit scoring");

            ScoringRequestDTO scoringRequest = ScoringRequestDTO.builder()
                    .customerId(customerId)
                    .applicationId(applicationId)
                    .monthlyIncome(monthlyIncome)
                    .age(age)
                    .tenureYears(tenureYears)
                    .employmentType(employmentType)
                    .nik(nik)
                    .build();

            ScoringResponseDTO scoringResponse = scoringServiceClient.calculateScore(scoringRequest);
            log.info("Scoring completed for application: {}, score: {}, recommendation: {}",
                    applicationId, scoringResponse.getScoreValue(), scoringResponse.getApprovalRecommendation());

            String recommendation = scoringResponse.getApprovalRecommendation();

            switch (recommendation) {
                case "APPROVE":
                    applicationService.updateApplicationStatus(applicationId, ApplicationStatus.APPROVED,
                            String.format("Auto approved with score: %d", scoringResponse.getScoreValue()));

                    Application application = applicationRepository.findById(applicationId).orElse(null);
                    if (application != null) {
                        application.setApprovedLimit(scoringResponse.getLimitRecommendation());
                        applicationRepository.save(application);
                    }
                    break;

                case "REJECT":
                    applicationService.updateApplicationStatus(applicationId, ApplicationStatus.REJECTED,
                            String.format("Auto rejected with score: %d. Reason: %s",
                                    scoringResponse.getScoreValue(), scoringResponse.getMessage()));
                    break;

                case "MANUAL_REVIEW":
                    log.info("Application {} requires manual review", applicationId);
                    break;

                default:
                    log.warn("Unknown recommendation: {}", recommendation);
            }

        } catch (Exception e) {
            log.error("Error processing scoring for application {}: {}", applicationId, e.getMessage());
            applicationService.updateApplicationStatus(applicationId, ApplicationStatus.VERIFICATION,
                    "Scoring failed: " + e.getMessage());
        }
    }
}
