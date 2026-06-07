package com.creditqu.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private String channel;
    private String title;
    private String content;
    private String recipient;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
