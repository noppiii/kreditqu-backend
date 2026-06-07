package com.creditqu.application_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusDTO {
    private String applicationNumber;
    private String status;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
}
