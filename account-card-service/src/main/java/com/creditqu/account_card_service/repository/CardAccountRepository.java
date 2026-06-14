package com.creditqu.account_card_service.repository;

import com.creditqu.account_card_service.entity.CardAccount;
import com.creditqu.common_module.constant.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    Optional<CardAccount> findByAccountNumber(String accountNumber);

    List<CardAccount> findByCustomerId(Long customerId);

    List<CardAccount> findByCustomerIdAndStatus(Long customerId, AccountStatus status);

    Optional<CardAccount> findByApplicationId(Long applicationId);

    @Query(value = "SELECT * FROM card_accounts ca WHERE ca.customer_id = :customerId ORDER BY ca.created_at DESC LIMIT 1",
            nativeQuery = true)
    Optional<CardAccount> findLatestByCustomerId(@Param("customerId") Long customerId);

    boolean existsByCustomerIdAndStatus(Long customerId, AccountStatus status);
}
