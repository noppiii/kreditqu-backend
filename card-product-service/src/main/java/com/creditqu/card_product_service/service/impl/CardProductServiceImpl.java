package com.creditqu.card_product_service.service.impl;

import com.creditqu.card_product_service.dto.CardProductResponseDTO;
import com.creditqu.card_product_service.dto.EligibilityRequestDTO;
import com.creditqu.card_product_service.dto.EligibilityResponseDTO;
import com.creditqu.card_product_service.entity.CardProduct;
import com.creditqu.card_product_service.entity.ProductFeature;
import com.creditqu.card_product_service.repository.CardProductRepository;
import com.creditqu.card_product_service.repository.ProductFeatureRepository;
import com.creditqu.card_product_service.service.CardProductService;
import com.creditqu.common_module.constant.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardProductServiceImpl implements CardProductService {

    private final CardProductRepository cardProductRepository;
    private final ProductFeatureRepository productFeatureRepository;

    @Override
    public List<CardProductResponseDTO> getAllActiveProducts() {
        log.info("Fetching all active card products");
        List<CardProduct> products = cardProductRepository.findByStatus(ProductStatus.ACTIVE);
        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CardProductResponseDTO getProductByCode(String productCode) {
        log.info("Fetching product by code: {}", productCode);
        CardProduct product = cardProductRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productCode));
        return mapToResponseDTO(product);
    }

    @Override
    public EligibilityResponseDTO checkEligibility(EligibilityRequestDTO request) {
        log.info("Checking eligibility for product: {}, income: {}, score: {}",
                request.getProductCode(), request.getMonthlyIncome(), request.getCreditScore());

        CardProduct product = cardProductRepository.findByProductCode(request.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductCode()));

        boolean incomeEligible = request.getMonthlyIncome().compareTo(product.getMinIncome()) >= 0;
        boolean scoreEligible = product.getMinCreditScore() == null ||
                (request.getCreditScore() != null && request.getCreditScore() >= product.getMinCreditScore());

        boolean eligible = incomeEligible && scoreEligible && product.getStatus() == ProductStatus.ACTIVE;

        String message;
        BigDecimal suggestedLimit = BigDecimal.ZERO;

        if (eligible) {
            suggestedLimit = request.getMonthlyIncome().multiply(BigDecimal.valueOf(3));
            if (suggestedLimit.compareTo(product.getMaxLimit()) > 0) {
                suggestedLimit = product.getMaxLimit();
            }
            if (suggestedLimit.compareTo(product.getDefaultLimit()) < 0) {
                suggestedLimit = product.getDefaultLimit();
            }
            message = "Anda memenuhi syarat untuk " + product.getProductName();
        } else if (!incomeEligible) {
            message = "Penghasilan Anda belum memenuhi syarat minimal untuk produk " + product.getProductName();
        } else {
            message = "Skor kredit Anda belum memenuhi syarat untuk produk " + product.getProductName();
        }

        return EligibilityResponseDTO.builder()
                .eligible(eligible)
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .message(message)
                .suggestedLimit(suggestedLimit)
                .build();
    }

    private CardProductResponseDTO mapToResponseDTO(CardProduct product) {
        List<String> features = productFeatureRepository.findByProductId(product.getId())
                .stream()
                .map(ProductFeature::getFeatureCode)
                .collect(Collectors.toList());

        return CardProductResponseDTO.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .description(product.getDescription())
                .minIncome(product.getMinIncome())
                .minCreditScore(product.getMinCreditScore())
                .defaultLimit(product.getDefaultLimit())
                .maxLimit(product.getMaxLimit())
                .annualFee(product.getAnnualFee())
                .annualFeeGracePeriod(product.getAnnualFeeGracePeriod())
                .interestRate(product.getInterestRate())
                .latePenaltyRate(product.getLatePenaltyRate())
                .cashbackPercentage(product.getCashbackPercentage())
                .rewardsMultiplier(product.getRewardsMultiplier())
                .status(product.getStatus().name())
                .features(features)
                .build();
    }
}
