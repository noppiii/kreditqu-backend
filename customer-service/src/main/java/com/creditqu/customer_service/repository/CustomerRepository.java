package com.creditqu.customer_service.repository;

import com.creditqu.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByNik(String nik);
    Optional<Customer> findByCustomerNumber(String customerNumber);
    boolean existsByEmail(String email);
    boolean existsByNik(String nik);
}
