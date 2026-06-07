package com.creditqu.notification_service.controller;

import com.creditqu.notification_service.dto.NotificationRequestDTO;
import com.creditqu.notification_service.dto.NotificationResponseDTO;
import com.creditqu.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> sendNotification(@Valid @RequestBody NotificationRequestDTO request) {
        NotificationResponseDTO response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/welcome")
    public ResponseEntity<NotificationResponseDTO> sendWelcomeEmail(@RequestParam Long userId, @RequestParam String email, @RequestParam String name) {
        NotificationResponseDTO response = notificationService.sendWelcomeEmail(userId, email, name);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp")
    public ResponseEntity<NotificationResponseDTO> sendOtp(@RequestParam String email, @RequestParam String otp) {
        NotificationResponseDTO response = notificationService.sendOtpEmail(email, otp);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/application-received")
    public ResponseEntity<NotificationResponseDTO> sendApplicationReceived(@RequestParam String email, @RequestParam String name, @RequestParam String applicationNumber) {
        NotificationResponseDTO response = notificationService.sendApplicationReceivedEmail(email, name, applicationNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approval")
    public ResponseEntity<NotificationResponseDTO> sendApproval(@RequestParam String email, @RequestParam String name, @RequestParam String cardNumberMasked, @RequestParam String limit) {
        NotificationResponseDTO response = notificationService.sendApprovalEmail(email, name, cardNumberMasked, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shipped")
    public ResponseEntity<NotificationResponseDTO> sendCardShipped(@RequestParam String email, @RequestParam String name, @RequestParam String trackingNumber) {
        NotificationResponseDTO response = notificationService.sendCardShippedEmail(email, name, trackingNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        List<NotificationResponseDTO> notifications = notificationService.getUserNotifications(userId, page, size);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification marked as read");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "notification-service");
        return ResponseEntity.ok(health);
    }
}
