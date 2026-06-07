package com.creditqu.billing_payment_service.entity;

import com.creditqu.common_module.constant.PaymentStatus;
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
@Table(name = "billing_cycles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "cycle_month", nullable = false)
    private Integer cycleMonth;

    @Column(name = "cycle_year", nullable = false)
    private Integer cycleYear;

    @Column(name = "statement_date", nullable = false)
    private LocalDate statementDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "previous_balance", precision = 15, scale = 2)
    private BigDecimal previousBalance = BigDecimal.ZERO;

    @Column(name = "total_charges", precision = 15, scale = 2)
    private BigDecimal totalCharges = BigDecimal.ZERO;

    @Column(name = "total_payments", precision = 15, scale = 2)
    private BigDecimal totalPayments = BigDecimal.ZERO;

    @Column(name = "interest_charged", precision = 15, scale = 2)
    private BigDecimal interestCharged = BigDecimal.ZERO;

    @Column(name = "late_fee", precision = 15, scale = 2)
    private BigDecimal lateFee = BigDecimal.ZERO;

    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "minimum_payment", nullable = false, precision = 15, scale = 2)
    private BigDecimal minimumPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "statement_pdf_url", length = 500)
    private String statementPdfUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
