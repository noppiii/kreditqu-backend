package com.creditqu.bff_card.service;

import com.creditqu.bff_card.dto.*;

import java.util.List;

public interface CardApplicationService {

    CreditCardApplicationResponseDTO submitApplication(CreditCardApplicationRequestDTO request);
    EligibilityResponseDTO checkEligibility(EligibilityRequestDTO request);
    List<ProductResponseDTO> getAvailableProducts();
}
