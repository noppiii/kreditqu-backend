package com.creditqu.application_service.controller;

import com.creditqu.application_service.dto.ApplicationRequestDTO;
import com.creditqu.application_service.dto.ApplicationResponseDTO;
import com.creditqu.application_service.dto.ApplicationStatusDTO;
import com.creditqu.application_service.service.ApplicationService;
import com.creditqu.common_module.constant.ApplicationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> submitApplication(@Valid @RequestBody ApplicationRequestDTO request) {
        ApplicationResponseDTO response = applicationService.submitApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/number/{applicationNumber}")
    public ResponseEntity<ApplicationResponseDTO> getApplicationByNumber(@PathVariable String applicationNumber) {
        return ResponseEntity.ok(applicationService.getApplicationByNumber(applicationNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(applicationService.getApplicationsByCustomerId(customerId));
    }

    @GetMapping("/status/{applicationNumber}")
    public ResponseEntity<ApplicationStatusDTO> getApplicationStatus(@PathVariable String applicationNumber) {
        return ResponseEntity.ok(applicationService.getApplicationStatus(applicationNumber));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id, status, notes));
    }
}
