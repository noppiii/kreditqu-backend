package com.creditqu.notification_service.repository;

import com.creditqu.common_module.constant.NotificationStatus;
import com.creditqu.common_module.constant.NotificationType;
import com.creditqu.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndStatusAndNotificationType(Long userId, NotificationStatus status, NotificationType type);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = :status, n.sentAt = :sentAt WHERE n.id = :id")
    void updateStatus(@Param("id") Long id,
                      @Param("status") NotificationStatus status,
                      @Param("sentAt") LocalDateTime sentAt);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.readAt = :readAt WHERE n.id = :id")
    void markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);

    long countByUserIdAndStatusAndCreatedAtAfter(Long userId, NotificationStatus status, LocalDateTime since);

    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, NotificationStatus status);

    List<Notification> findByUserIdAndNotificationTypeOrderByCreatedAtDesc(Long userId, NotificationType type);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    void updateStatusOnly(@Param("id") Long id, @Param("status") NotificationStatus status);
}