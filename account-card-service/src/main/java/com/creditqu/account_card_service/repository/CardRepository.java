package com.creditqu.account_card_service.repository;

import com.creditqu.account_card_service.entity.Card;
import com.creditqu.common_module.constant.CardStatus;
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
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);

    List<Card> findByAccountId(Long accountId);

    List<Card> findByAccountIdAndIsPrimaryTrue(Long accountId);

    List<Card> findByStatus(CardStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.status = :status, c.activatedAt = :activatedAt WHERE c.id = :id")
    void activateCard(@Param("id") Long id,
                      @Param("status") CardStatus status,
                      @Param("activatedAt") LocalDateTime activatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.status = :status, c.blockedAt = :blockedAt, c.blockReason = :reason WHERE c.id = :id")
    void blockCard(@Param("id") Long id,
                   @Param("status") CardStatus status,
                   @Param("blockedAt") LocalDateTime blockedAt,
                   @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.pinHash = :pinHash WHERE c.id = :id")
    void updatePinHash(@Param("id") Long id, @Param("pinHash") String pinHash);

    boolean existsByCardNumber(String cardNumber);
}