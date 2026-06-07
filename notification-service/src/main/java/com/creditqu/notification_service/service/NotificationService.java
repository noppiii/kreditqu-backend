package com.creditqu.notification_service.service;

import com.creditqu.notification_service.dto.NotificationRequestDTO;
import com.creditqu.notification_service.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {
    NotificationResponseDTO sendNotification(NotificationRequestDTO request);
    NotificationResponseDTO sendWelcomeEmail(Long userId, String email, String name);
    NotificationResponseDTO sendOtpEmail(String email, String otp);
    NotificationResponseDTO sendApplicationReceivedEmail(String email, String name, String applicationNumber);
    NotificationResponseDTO sendApprovalEmail(String email, String name, String cardNumberMasked, String limit);
    NotificationResponseDTO sendCardShippedEmail(String email, String name, String trackingNumber);
    NotificationResponseDTO sendTransactionNotification(Long userId, String recipient, String amount, String merchant);
    List<NotificationResponseDTO> getUserNotifications(Long userId, int page, int size);
    void markAsRead(Long notificationId);
}
