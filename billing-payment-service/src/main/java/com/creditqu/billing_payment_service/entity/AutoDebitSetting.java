package com.creditqu.billing_payment_service.entity;

import com.creditqu.common_module.constant.DebitType;
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
@Table(name = "auto_debit_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoDebitSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(name = "is_enabled")
    private Boolean isEnabled = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "debit_type")
    private DebitType debitType;

    @Column(name = "fixed_amount", precision = 15, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "source_bank", length = 50)
    private String sourceBank;

    @Column(name = "source_account_number", length = 30)
    private String sourceAccountNumber;

    @Column(name = "last_executed")
    private LocalDateTime lastExecuted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
