package com.creditqu.account_card_service.entity;

import com.creditqu.common_module.constant.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "product_code", nullable = false, length = 20)
    private String productCode;

    @Column(name = "current_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentLimit;

    @Column(name = "outstanding_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Column(name = "available_limit", precision = 15, scale = 2)
    private BigDecimal availableLimit;

    @Column(name = "billing_cycle_day")
    @Builder.Default
    private Integer billingCycleDay = 1;

    @Column(name = "due_date_offset")
    @Builder.Default
    private Integer dueDateOffset = 14;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}