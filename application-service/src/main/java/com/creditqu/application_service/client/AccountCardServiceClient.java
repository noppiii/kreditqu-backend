package com.creditqu.application_service.client;

import com.creditqu.application_service.dto.CreateAccountRequestDTO;
import com.creditqu.application_service.dto.CreateAccountResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-card-service")
public interface AccountCardServiceClient {

    @PostMapping("/api/accounts/create")
    CreateAccountResponseDTO createAccountAndCard(@RequestBody CreateAccountRequestDTO request);
}