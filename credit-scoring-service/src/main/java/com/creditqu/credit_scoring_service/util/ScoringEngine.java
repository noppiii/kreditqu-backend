package com.creditqu.credit_scoring_service.util;

import com.creditqu.credit_scoring_service.constant.ApprovalRecommendation;
import com.creditqu.credit_scoring_service.constant.ScoreRating;
import com.creditqu.credit_scoring_service.dto.CustomerDataDTO;
import com.creditqu.credit_scoring_service.dto.ScoringRequestDTO;
import com.creditqu.credit_scoring_service.entity.ScoringRule;
import com.creditqu.credit_scoring_service.repository.ScoringRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoringEngine {

    private final ScoringRuleRepository scoringRuleRepository;
    private final ExternalBureau externalBureau;

    @Value("${scoring.rules.min-income-score:100}")
    private int minIncomeScore;

    @Value("${scoring.rules.max-income-score:300}")
    private int maxIncomeScore;

    @Value("${scoring.rules.min-age-score:50}")
    private int minAgeScore;

    @Value("${scoring.rules.max-age-score:150}")
    private int maxAgeScore;

    @Value("${scoring.rules.min-tenure-score:50}")
    private int minTenureScore;

    @Value("${scoring.rules.max-tenure-score:100}")
    private int maxTenureScore;

    public ScoringResult calculateScore(ScoringRequestDTO request, CustomerDataDTO customer) {
        log.info("Calculating credit score for customer: {}", request.getCustomerId());

        int internalScore = calculateInternalScore(request, customer);
        log.info("Internal score: {}", internalScore);

        int externalScore = externalBureau.checkExternalBureau(request.getNik());
        log.info("External score: {}", externalScore);

        int finalScore = (int) Math.round((internalScore * 0.7) + (externalScore * 0.3));
        log.info("Final score: {}", finalScore);

        ScoreRating rating = ScoreRating.fromScore(finalScore);
        log.info("Rating: {}", rating);

        ApprovalRecommendation recommendation = determineRecommendation(finalScore);
        log.info("Recommendation: {}", recommendation);

        BigDecimal suggestedLimit = calculateSuggestedLimit(request.getMonthlyIncome(), finalScore);

        return ScoringResult.builder()
                .scoreValue(finalScore)
                .rating(rating)
                .internalScore(internalScore)
                .externalScore(externalScore)
                .approvalRecommendation(recommendation)
                .limitRecommendation(suggestedLimit)
                .build();
    }

    private int calculateInternalScore(ScoringRequestDTO request, CustomerDataDTO customer) {
        int totalScore = 0;

        List<ScoringRule> rules = scoringRuleRepository.findByIsActiveTrueOrderByPriorityDesc();

        for (ScoringRule rule : rules) {
            if (matchesRule(rule, request, customer)) {
                totalScore += rule.getScorePoint();
                log.debug("Rule '{}' matched: +{} points", rule.getRuleName(), rule.getScorePoint());
            }
        }

        return Math.min(850, Math.max(0, totalScore));
    }

    private boolean matchesRule(ScoringRule rule, ScoringRequestDTO request, CustomerDataDTO customer) {
        if (rule.getMinIncome() != null && request.getMonthlyIncome().compareTo(rule.getMinIncome()) < 0) {
            return false;
        }
        if (rule.getMaxIncome() != null && request.getMonthlyIncome().compareTo(rule.getMaxIncome()) > 0) {
            return false;
        }

        if (rule.getMinAge() != null && customer.getAge() < rule.getMinAge()) {
            return false;
        }
        if (rule.getMaxAge() != null && customer.getAge() > rule.getMaxAge()) {
            return false;
        }

        if (rule.getMinTenure() != null && request.getTenureYears() < rule.getMinTenure()) {
            return false;
        }
        if (rule.getMaxTenure() != null && request.getTenureYears() > rule.getMaxTenure()) {
            return false;
        }

        if (rule.getEmploymentType() != null && !rule.getEmploymentType().equals(request.getEmploymentType())) {
            return false;
        }

        return true;
    }

    private ApprovalRecommendation determineRecommendation(int score) {
        if (score >= 600) {
            return ApprovalRecommendation.APPROVE;
        } else if (score < 450) {
            return ApprovalRecommendation.REJECT;
        } else {
            return ApprovalRecommendation.MANUAL_REVIEW;
        }
    }

    private BigDecimal calculateSuggestedLimit(BigDecimal monthlyIncome, int score) {
        double multiplier;
        if (score >= 800) {
            multiplier = 6;
        } else if (score >= 700) {
            multiplier = 5;
        } else if (score >= 600) {
            multiplier = 4;
        } else if (score >= 500) {
            multiplier = 3;
        } else {
            multiplier = 2;
        }

        BigDecimal suggestedLimit = monthlyIncome.multiply(BigDecimal.valueOf(multiplier));

        BigDecimal maxLimit = new BigDecimal("500000000");
        if (suggestedLimit.compareTo(maxLimit) > 0) {
            suggestedLimit = maxLimit;
        }

        return suggestedLimit;
    }

    @lombok.Builder
    @lombok.Data
    public static class ScoringResult {
        private Integer scoreValue;
        private ScoreRating rating;
        private Integer internalScore;
        private Integer externalScore;
        private ApprovalRecommendation approvalRecommendation;
        private BigDecimal limitRecommendation;
    }
}
