package com.creditqu.account_card_service.repository;

import com.creditqu.account_card_service.entity.VirtualAccount;
import com.creditqu.common_module.constant.VirtualAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {

    List<VirtualAccount> findByAccountId(Long accountId);

    Optional<VirtualAccount> findByVirtualAccountNumber(String virtualAccountNumber);

    List<VirtualAccount> findByStatusAndExpiredAtBefore(VirtualAccountStatus status, LocalDateTime expiredAt);

    @Modifying
    @Transactional
    @Query("UPDATE VirtualAccount va SET va.status = :status WHERE va.expiredAt < :now")
    int expireOldVirtualAccounts(@Param("status") VirtualAccountStatus status, @Param("now") LocalDateTime now);
}
