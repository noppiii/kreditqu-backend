package com.creditqu.application_service.service;

import com.creditqu.application_service.dto.ScoringResponseDTO;

public interface ApplicationApprovalService {

    void processApproval(Long applicationId, ScoringResponseDTO scoringResponse);
}
