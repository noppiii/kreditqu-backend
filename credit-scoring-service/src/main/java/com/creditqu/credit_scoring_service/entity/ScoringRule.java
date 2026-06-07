package com.creditqu.credit_scoring_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "scoring_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "min_income", precision = 15, scale = 2)
    private BigDecimal minIncome;

    @Column(name = "max_income", precision = 15, scale = 2)
    private BigDecimal maxIncome;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "min_tenure")
    private Integer minTenure;

    @Column(name = "score_point", nullable = false)
    private Integer scorePoint;

    @Column(nullable = false)
    private Integer priority = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;
}