package com.creditqu.bff_card.client;

import com.creditqu.bff_card.dto.EligibilityRequestDTO;
import com.creditqu.bff_card.dto.EligibilityResponseDTO;
import com.creditqu.bff_card.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "card-product-service")
public interface CardProductServiceClient {

    @GetMapping("/api/products")
    List<ProductResponseDTO> getAllActiveProducts();

    @GetMapping("/api/products/{productCode}")
    ProductResponseDTO getProductByCode(@PathVariable("productCode") String productCode);

    @PostMapping("/api/products/eligibility")
    EligibilityResponseDTO checkEligibility(@RequestBody EligibilityRequestDTO request);
}
