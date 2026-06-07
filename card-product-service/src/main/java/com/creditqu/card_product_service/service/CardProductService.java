package com.creditqu.card_product_service.service;

import com.creditqu.card_product_service.dto.CardProductResponseDTO;
import com.creditqu.card_product_service.dto.EligibilityRequestDTO;
import com.creditqu.card_product_service.dto.EligibilityResponseDTO;

import java.util.List;

public interface CardProductService {
    List<CardProductResponseDTO> getAllActiveProducts();
    CardProductResponseDTO getProductByCode(String productCode);
    EligibilityResponseDTO checkEligibility(EligibilityRequestDTO request);
}
