package com.creditqu.bff_card.client;

import com.creditqu.bff_card.dto.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/{id}")
    CustomerResponseDTO getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/api/customers/exists/email")
    Boolean existsByEmail(@RequestParam("email") String email);
}
