package com.creditqu.customer_service.entity;

import com.creditqu.common_module.constant.EmploymentType;
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
@Table(name = "customer_employment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEmployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(length = 50)
    private String industry;

    @Column(length = 50)
    private String position;

    @Column(name = "monthly_income", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(name = "income_source", length = 100)
    private String incomeSource;

    @Column(name = "work_phone", length = 15)
    private String workPhone;

    @Column(name = "work_email", length = 100)
    private String workEmail;

    @Column(name = "tenure_years")
    private Integer tenureYears;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
