package com.creditqu.credit_scoring_service.entity;

import com.creditqu.common_module.constant.ApprovalRecommendation;
import com.creditqu.common_module.constant.ScoreRating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_scores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "score_value", nullable = false)
    private Integer scoreValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScoreRating rating;

    @Column(name = "internal_score")
    private Integer internalScore;

    @Column(name = "external_score")
    private Integer externalScore;

    @Column(name = "external_bureau", length = 50)
    private String externalBureau;

    @Column(name = "limit_recommendation", precision = 15, scale = 2)
    private BigDecimal limitRecommendation;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_recommendation")
    private ApprovalRecommendation approvalRecommendation;

    @Column(name = "scoring_date", nullable = false)
    private LocalDate scoringDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
