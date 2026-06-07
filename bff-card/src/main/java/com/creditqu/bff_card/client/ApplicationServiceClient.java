package com.creditqu.bff_card.client;

import com.creditqu.bff_card.dto.ApplicationRequestDTO;
import com.creditqu.bff_card.dto.ApplicationResponseDTO;
import com.creditqu.bff_card.dto.ApplicationStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "application-service")
public interface ApplicationServiceClient {

    @PostMapping("/api/applications")
    ApplicationResponseDTO submitApplication(@RequestBody ApplicationRequestDTO request);

    @GetMapping("/api/applications/customer/{customerId}")
    List<ApplicationResponseDTO> getApplicationsByCustomerId(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/applications/status/{applicationNumber}")
    ApplicationStatusDTO getApplicationStatus(@PathVariable("applicationNumber") String applicationNumber);
}
