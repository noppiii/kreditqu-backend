package com.creditqu.account_card_service.util;

import com.creditqu.account_card_service.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardNumberGeneratorUtil {

    private final LuhnAlgorithm luhnAlgorithm;
    private final CardRepository cardRepository;

    private static final Map<String, String> BIN_PREFIXES = new HashMap<>();

    static {
        BIN_PREFIXES.put("GOLD", "412345");
        BIN_PREFIXES.put("PLATINUM", "512345");
        BIN_PREFIXES.put("TITANIUM", "612345");
        BIN_PREFIXES.put("DEFAULT", "412345");
    }

    /**
     * Generate a unique valid card number
     */
    public String generateUniqueCardNumber(String productCode) {
        String prefix = BIN_PREFIXES.getOrDefault(productCode, BIN_PREFIXES.get("DEFAULT"));
        int maxAttempts = 10;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            String cardNumber = luhnAlgorithm.generateCardNumber(prefix, 16);

            if (!cardRepository.existsByCardNumber(cardNumber)) {
                log.info("Generated unique card number: {}", maskCardNumber(cardNumber));
                return cardNumber;
            }
            log.debug("Card number collision, retrying...");
        }

        throw new RuntimeException("Failed to generate unique card number after " + maxAttempts + " attempts");
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return "****";
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}