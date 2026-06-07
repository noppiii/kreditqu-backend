package com.creditqu.credit_scoring_service.client;

import com.creditqu.credit_scoring_service.dto.CustomerDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/{id}")
    CustomerDataDTO getCustomerById(@PathVariable("id") Long id);
}
