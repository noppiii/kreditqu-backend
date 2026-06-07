package com.creditqu.notification_service.service;

public interface PushNotificationService {

    void sendPushNotification(String deviceToken, String title, String body);

}
