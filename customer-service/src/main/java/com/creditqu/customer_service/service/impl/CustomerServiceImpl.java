package com.creditqu.customer_service.service.impl;

import com.creditqu.customer_service.dto.CustomerResponseDTO;
import com.creditqu.customer_service.dto.RegisterRequestDTO;
import com.creditqu.customer_service.entity.Customer;
import com.creditqu.customer_service.exception.CustomerAlreadyExistsException;
import com.creditqu.customer_service.mapper.CustomerMapper;
import com.creditqu.customer_service.repository.CustomerRepository;
import com.creditqu.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String CUSTOMER_NUMBER_PREFIX = "CUST";
    private static int customerCounter = 1;

    @Override
    @Transactional
    public CustomerResponseDTO register(RegisterRequestDTO request) {
        log.info("Registering new customer with email: {}", request.getEmail());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Email already registered: " + request.getEmail());
        }

        if (customerRepository.existsByNik(request.getNik())) {
            throw new CustomerAlreadyExistsException("NIK already registered");
        }

        String customerNumber = generateCustomerNumber();

        Customer customer = customerMapper.toEntity(request);
        customer.setCustomerNumber(customerNumber);
        customer.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        customer.setIsActive(true);

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer registered successfully with ID: {}", savedCustomer.getId());

        return customerMapper.toResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNik(String nik) {
        return customerRepository.existsByNik(nik);
    }

    private synchronized String generateCustomerNumber() {
        String yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String counter = String.format("%04d", customerCounter++);
        return CUSTOMER_NUMBER_PREFIX + yearMonth + counter;
    }
}
