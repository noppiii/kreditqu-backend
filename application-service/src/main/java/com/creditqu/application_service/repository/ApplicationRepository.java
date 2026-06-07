package com.creditqu.application_service.repository;

import com.creditqu.application_service.entity.Application;
import com.creditqu.common_module.constant.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByApplicationNumber(String applicationNumber);
    List<Application> findByCustomerId(Long customerId);
    Page<Application> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
    List<Application> findByStatus(ApplicationStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Application a SET a.status = :status, a.verifiedAt = :verifiedAt WHERE a.id = :id")
    void updateStatusToVerified(@Param("id") Long id,
                                @Param("status") ApplicationStatus status,
                                @Param("verifiedAt") LocalDateTime verifiedAt);

    @Modifying
    @Transactional
    @Query("UPDATE Application a SET a.status = :status, a.approvedAt = :approvedAt, a.approvedLimit = :limit WHERE a.id = :id")
    void updateStatusToApproved(@Param("id") Long id,
                                @Param("status") ApplicationStatus status,
                                @Param("approvedAt") LocalDateTime approvedAt,
                                @Param("limit") BigDecimal limit);

    @Modifying
    @Transactional
    @Query("UPDATE Application a SET a.status = :status, a.rejectedAt = :rejectedAt, a.rejectionReason = :reason WHERE a.id = :id")
    void updateStatusToRejected(@Param("id") Long id,
                                @Param("status") ApplicationStatus status,
                                @Param("rejectedAt") LocalDateTime rejectedAt,
                                @Param("reason") String reason);
}
