package com.creditqu.customer_service.mapper;

import com.creditqu.customer_service.dto.CustomerResponseDTO;
import com.creditqu.customer_service.dto.RegisterRequestDTO;
import com.creditqu.customer_service.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(RegisterRequestDTO request) {
        return Customer.builder()
                .fullName(request.getFullName())
                .nik(request.getNik())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .placeOfBirth(request.getPlaceOfBirth())
                .dateOfBirth(request.getDateOfBirth())
                .gender(Customer.Gender.valueOf(request.getGender()))
                .build();
    }

    public CustomerResponseDTO toResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .customerNumber(customer.getCustomerNumber())
                .fullName(customer.getFullName())
                .nik(customer.getNik())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .placeOfBirth(customer.getPlaceOfBirth())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender().name())
                .isActive(customer.getIsActive())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
