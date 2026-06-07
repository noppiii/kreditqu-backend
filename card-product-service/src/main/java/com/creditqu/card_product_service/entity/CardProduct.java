package com.creditqu.card_product_service.entity;

import com.creditqu.common_module.constant.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", unique = true, nullable = false, length = 20)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "min_income", nullable = false, precision = 15, scale = 2)
    private BigDecimal minIncome;

    @Column(name = "min_credit_score")
    private Integer minCreditScore;

    @Column(name = "default_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal defaultLimit;

    @Column(name = "max_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxLimit;

    @Column(name = "annual_fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal annualFee;

    @Column(name = "annual_fee_grace_period")
    private Integer annualFeeGracePeriod = 12;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "late_penalty_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal latePenaltyRate;

    @Column(name = "cashback_percentage", precision = 5, scale = 2)
    private BigDecimal cashbackPercentage = BigDecimal.ZERO;

    @Column(name = "rewards_multiplier", precision = 3, scale = 2)
    private BigDecimal rewardsMultiplier = BigDecimal.ONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
