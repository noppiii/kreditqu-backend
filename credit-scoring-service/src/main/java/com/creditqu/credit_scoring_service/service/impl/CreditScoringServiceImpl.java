package com.creditqu.credit_scoring_service.service.impl;

import com.creditqu.credit_scoring_service.client.CustomerServiceClient;
import com.creditqu.credit_scoring_service.constant.ApprovalRecommendation;
import com.creditqu.credit_scoring_service.dto.CustomerDataDTO;
import com.creditqu.credit_scoring_service.dto.ScoringRequestDTO;
import com.creditqu.credit_scoring_service.dto.ScoringResponseDTO;
import com.creditqu.credit_scoring_service.entity.CreditScore;
import com.creditqu.credit_scoring_service.repository.CreditScoreRepository;
import com.creditqu.credit_scoring_service.service.CreditScoringService;
import com.creditqu.credit_scoring_service.util.ScoringEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditScoringServiceImpl implements CreditScoringService {

    private final CreditScoreRepository creditScoreRepository;
    private final CustomerServiceClient customerServiceClient;
    private final ScoringEngine scoringEngine;

    @Override
    @Transactional
    public ScoringResponseDTO calculateScore(ScoringRequestDTO request) {
        log.info("Processing scoring request for customer: {}, application: {}",
                request.getCustomerId(), request.getApplicationId());

        CustomerDataDTO customer = customerServiceClient.getCustomerById(request.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found: " + request.getCustomerId());
        }

        ScoringEngine.ScoringResult result = scoringEngine.calculateScore(request, customer);

        CreditScore creditScore = CreditScore.builder()
                .customerId(request.getCustomerId())
                .applicationId(request.getApplicationId())
                .scoreValue(result.getScoreValue())
                .rating(result.getRating())
                .internalScore(result.getInternalScore())
                .externalScore(result.getExternalScore())
                .approvalRecommendation(result.getApprovalRecommendation())
                .limitRecommendation(result.getLimitRecommendation())
                .scoringDate(LocalDate.now())
                .notes("Calculated by automated scoring engine")
                .build();

        creditScoreRepository.save(creditScore);
        log.info("Credit score saved with ID: {}", creditScore.getId());

        String message = buildMessage(result.getApprovalRecommendation(), result.getScoreValue());

        return ScoringResponseDTO.builder()
                .customerId(request.getCustomerId())
                .applicationId(request.getApplicationId())
                .scoreValue(result.getScoreValue())
                .rating(result.getRating().name())
                .internalScore(result.getInternalScore())
                .externalScore(result.getExternalScore())
                .approvalRecommendation(result.getApprovalRecommendation().name())
                .limitRecommendation(result.getLimitRecommendation())
                .scoringDate(LocalDate.now())
                .message(message)
                .build();
    }

    @Override
    public ScoringResponseDTO getScoreByApplicationId(Long applicationId) {
        CreditScore creditScore = creditScoreRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Score not found for application: " + applicationId));

        return ScoringResponseDTO.builder()
                .customerId(creditScore.getCustomerId())
                .applicationId(creditScore.getApplicationId())
                .scoreValue(creditScore.getScoreValue())
                .rating(creditScore.getRating().name())
                .internalScore(creditScore.getInternalScore())
                .externalScore(creditScore.getExternalScore())
                .approvalRecommendation(creditScore.getApprovalRecommendation().name())
                .limitRecommendation(creditScore.getLimitRecommendation())
                .scoringDate(creditScore.getScoringDate())
                .build();
    }

    private String buildMessage(ApprovalRecommendation recommendation, int score) {
        switch (recommendation) {
            case APPROVE:
                return String.format("Congratulations! Your application has been approved with a credit score of %d.", score);
            case REJECT:
                return String.format("We regret to inform you that your application could not be approved with a credit score of %d.", score);
            case MANUAL_REVIEW:
                return String.format("Your application requires manual review with a credit score of %d.", score);
            default:
                return "Scoring completed successfully.";
        }
    }
}
