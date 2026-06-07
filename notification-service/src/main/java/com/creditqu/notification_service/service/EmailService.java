package com.creditqu.notification_service.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
    void sendHtmlEmail(String to, String subject, String htmlBody);
}
