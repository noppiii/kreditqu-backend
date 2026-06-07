package com.creditqu.customer_service.service;

import com.creditqu.customer_service.dto.CustomerResponseDTO;
import com.creditqu.customer_service.dto.RegisterRequestDTO;

public interface CustomerService {
    CustomerResponseDTO register(RegisterRequestDTO request);
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO getCustomerByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNik(String nik);
}
