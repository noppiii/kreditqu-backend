package com.creditqu.application_service.service.impl;

import com.creditqu.application_service.dto.ApplicationRequestDTO;
import com.creditqu.application_service.dto.ApplicationResponseDTO;
import com.creditqu.application_service.dto.ApplicationStatusDTO;
import com.creditqu.application_service.entity.Application;
import com.creditqu.application_service.entity.ApplicationStatusHistory;
import com.creditqu.application_service.repository.ApplicationRepository;
import com.creditqu.application_service.repository.ApplicationStatusHistoryRepository;
import com.creditqu.application_service.service.ApplicationService;
import com.creditqu.application_service.util.ApplicationNumberGenerator;
import com.creditqu.common_module.constant.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final ApplicationNumberGenerator numberGenerator;

    @Override
    @Transactional
    public ApplicationResponseDTO submitApplication(ApplicationRequestDTO request) {
        log.info("Submitting new application for customer: {}", request.getCustomerId());

        List<Application> existingApps = applicationRepository.findByCustomerId(request.getCustomerId());
        boolean hasPending = existingApps.stream()
                .anyMatch(app -> app.getStatus() == ApplicationStatus.SUBMITTED ||
                        app.getStatus() == ApplicationStatus.VERIFICATION);

        if (hasPending) {
            throw new RuntimeException("Anda masih memiliki pengajuan yang sedang diproses");
        }

        Application application = Application.builder()
                .applicationNumber(numberGenerator.generate())
                .customerId(request.getCustomerId())
                .productCode(request.getProductCode())
                .desiredLimit(request.getDesiredLimit())
                .monthlyIncome(request.getMonthlyIncome())
                .employmentType(request.getEmploymentType())
                .companyName(request.getCompanyName())
                .tenureYears(request.getTenureYears())
                .status(ApplicationStatus.SUBMITTED)
                .notes(request.getNotes())
                .submittedAt(LocalDateTime.now())
                .build();

        Application savedApplication = applicationRepository.save(application);
        log.info("Application saved with number: {}", savedApplication.getApplicationNumber());

        saveStatusHistory(savedApplication, null, ApplicationStatus.SUBMITTED.name(), "Initial submission");

        return mapToResponseDTO(savedApplication);
    }

    @Override
    public ApplicationResponseDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        return mapToResponseDTO(application);
    }

    @Override
    public ApplicationResponseDTO getApplicationByNumber(String applicationNumber) {
        Application application = applicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found with number: " + applicationNumber));
        return mapToResponseDTO(application);
    }

    @Override
    public List<ApplicationResponseDTO> getApplicationsByCustomerId(Long customerId) {
        List<Application> applications = applicationRepository.findByCustomerId(customerId);
        return applications.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationStatusDTO getApplicationStatus(String applicationNumber) {
        Application application = applicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationNumber));

        return ApplicationStatusDTO.builder()
                .applicationNumber(application.getApplicationNumber())
                .status(application.getStatus().name())
                .rejectionReason(application.getRejectionReason())
                .submittedAt(application.getSubmittedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public ApplicationResponseDTO updateApplicationStatus(Long id, ApplicationStatus newStatus, String notes) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));

        String oldStatus = application.getStatus().name();
        application.setStatus(newStatus);

        switch (newStatus) {
            case VERIFICATION:
                application.setVerifiedAt(LocalDateTime.now());
                break;
            case APPROVED:
                application.setApprovedAt(LocalDateTime.now());
                break;
            case REJECTED:
                application.setRejectedAt(LocalDateTime.now());
                application.setRejectionReason(notes);
                break;
            default:
                break;
        }

        Application updated = applicationRepository.save(application);
        saveStatusHistory(updated, oldStatus, newStatus.name(), notes);

        log.info("Application {} status updated from {} to {}",
                application.getApplicationNumber(), oldStatus, newStatus);

        return mapToResponseDTO(updated);
    }

    private void saveStatusHistory(Application application, String oldStatus, String newStatus, String notes) {
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy("SYSTEM")
                .notes(notes)
                .build();
        historyRepository.save(history);
    }

    private ApplicationResponseDTO mapToResponseDTO(Application application) {
        return ApplicationResponseDTO.builder()
                .id(application.getId())
                .applicationNumber(application.getApplicationNumber())
                .customerId(application.getCustomerId())
                .productCode(application.getProductCode())
                .desiredLimit(application.getDesiredLimit())
                .status(application.getStatus().name())
                .rejectionReason(application.getRejectionReason())
                .approvedLimit(application.getApprovedLimit())
                .submittedAt(application.getSubmittedAt())
                .approvedAt(application.getApprovedAt())
                .rejectedAt(application.getRejectedAt())
                .createdAt(application.getCreatedAt())
                .build();
    }
}