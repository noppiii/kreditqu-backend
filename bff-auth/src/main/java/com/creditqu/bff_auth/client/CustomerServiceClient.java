package com.creditqu.bff_auth.client;

import com.creditqu.bff_auth.dto.CustomerResponseDTO;
import com.creditqu.bff_auth.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @PostMapping("/api/customers/register")
    CustomerResponseDTO register(@RequestBody RegisterRequestDTO request);

    @GetMapping("/api/customers/by-email")
    CustomerResponseDTO getCustomerByEmail(@RequestParam("email") String email);

    @GetMapping("/api/customers/exists/email")
    Boolean existsByEmail(@RequestParam("email") String email);
}
