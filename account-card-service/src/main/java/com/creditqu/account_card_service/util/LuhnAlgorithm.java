package com.creditqu.account_card_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LuhnAlgorithm {

    /**
     * Generate a valid Luhn check digit for the given number
     */
    public int calculateCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        int checkDigit = (sum * 9) % 10;
        log.debug("Calculated check digit: {} for number: {}", checkDigit, number);
        return checkDigit;
    }

    /**
     * Validate if a card number is valid using Luhn algorithm
     */
    public boolean isValid(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        boolean isValid = (sum % 10 == 0);
        log.debug("Card number {} is valid: {}", maskCardNumber(cardNumber), isValid);
        return isValid;
    }

    /**
     * Generate a full valid card number with given prefix
     */
    public String generateCardNumber(String prefix, int length) {
        if (prefix == null || prefix.isEmpty()) {
            prefix = "4"; // Default Visa prefix
        }

        int remainingLength = length - prefix.length() - 1; // -1 for check digit
        StringBuilder sb = new StringBuilder(prefix);

        // Generate random digits for the middle part
        for (int i = 0; i < remainingLength; i++) {
            sb.append((int) (Math.random() * 10));
        }

        // Calculate and append check digit
        int checkDigit = calculateCheckDigit(sb.toString());
        sb.append(checkDigit);

        log.debug("Generated card number: {}", maskCardNumber(sb.toString()));
        return sb.toString();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return "****";
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}
