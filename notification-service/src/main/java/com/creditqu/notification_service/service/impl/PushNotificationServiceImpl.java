package com.creditqu.notification_service.service.impl;

import com.creditqu.notification_service.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

    @Value("${notification.push.enabled:false}")
    private boolean pushEnabled;

    @Override
    public void sendPushNotification(String deviceToken, String title, String body) {
        if (!pushEnabled) {
            log.info("Push notification disabled. Would send to {}: {} - {}", deviceToken, title, body);
            return;
        }

        try {
            // TODO: Implement push notification Firebase FCM
            log.info("Sending push notification to device: {}", deviceToken);
            log.info("Push notification sent successfully");
        } catch (Exception e) {
            log.error("Failed to send push notification: {}", e.getMessage());
            throw new RuntimeException("Push notification failed: " + e.getMessage());
        }
    }
}
