package com.creditqu.credit_scoring_service.repository;

import com.creditqu.credit_scoring_service.entity.ScoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ScoringRuleRepository extends JpaRepository<ScoringRule, Long> {
    List<ScoringRule> findByIsActiveTrueOrderByPriorityDesc();

    @Query("SELECT r FROM ScoringRule r WHERE r.isActive = true AND " +
            "(r.minIncome IS NULL OR r.minIncome <= :income) AND " +
            "(r.maxIncome IS NULL OR r.maxIncome >= :income) AND " +
            "(r.minAge IS NULL OR r.minAge <= :age) AND " +
            "(r.maxAge IS NULL OR r.maxAge >= :age) AND " +
            "(r.employmentType IS NULL OR r.employmentType = :employmentType)")
    List<ScoringRule> findMatchingRules(BigDecimal income, Integer age, String employmentType);
}
