package com.creditqu.customer_service.controller;

import com.creditqu.customer_service.dto.CustomerResponseDTO;
import com.creditqu.customer_service.dto.RegisterRequestDTO;
import com.creditqu.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        CustomerResponseDTO response = customerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@RequestParam String email) {
        CustomerResponseDTO response = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.existsByEmail(email));
    }
}
