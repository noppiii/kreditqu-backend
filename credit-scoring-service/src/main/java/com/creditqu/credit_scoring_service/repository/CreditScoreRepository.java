package com.creditqu.credit_scoring_service.repository;

import com.creditqu.credit_scoring_service.entity.CreditScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditScoreRepository extends JpaRepository<CreditScore, Long> {
    Optional<CreditScore> findByApplicationId(Long applicationId);
    List<CreditScore> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    Optional<CreditScore> findTopByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
