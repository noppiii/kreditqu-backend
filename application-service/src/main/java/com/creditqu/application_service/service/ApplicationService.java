package com.creditqu.application_service.service;

import com.creditqu.application_service.dto.ApplicationRequestDTO;
import com.creditqu.application_service.dto.ApplicationResponseDTO;
import com.creditqu.application_service.dto.ApplicationStatusDTO;
import com.creditqu.common_module.constant.ApplicationStatus;

import java.util.List;

public interface ApplicationService {
    ApplicationResponseDTO submitApplication(ApplicationRequestDTO request);
    ApplicationResponseDTO getApplicationById(Long id);
    ApplicationResponseDTO getApplicationByNumber(String applicationNumber);
    List<ApplicationResponseDTO> getApplicationsByCustomerId(Long customerId);
    ApplicationStatusDTO getApplicationStatus(String applicationNumber);
    ApplicationResponseDTO updateApplicationStatus(Long id, ApplicationStatus newStatus, String notes);
}
