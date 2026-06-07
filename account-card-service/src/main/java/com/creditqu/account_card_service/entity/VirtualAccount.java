package com.creditqu.account_card_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "virtual_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private CardAccount account;

    @Column(name = "virtual_account_number", unique = true, nullable = false, length = 30)
    private String virtualAccountNumber;

    @Column(name = "bank_code", nullable = false, length = 10)
    private String bankCode;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
