package com.creditqu.account_card_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Slf4j
public class CryptoUtil {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Hash CVV (3 digits) - CVV tidak boleh disimpan plain text
     */
    public String hashCvv(String cvv) {
        if (cvv == null || cvv.length() != 3) {
            throw new IllegalArgumentException("CVV must be 3 digits");
        }
        String hashed = passwordEncoder.encode(cvv);
        log.debug("CVV hashed successfully");
        return hashed;
    }

    /**
     * Verify CVV against stored hash
     */
    public boolean verifyCvv(String rawCvv, String hashedCvv) {
        boolean isValid = passwordEncoder.matches(rawCvv, hashedCvv);
        log.debug("CVV verification: {}", isValid);
        return isValid;
    }

    /**
     * Hash PIN (6 digits)
     */
    public String hashPin(String pin) {
        if (pin == null || pin.length() != 6) {
            throw new IllegalArgumentException("PIN must be 6 digits");
        }
        String hashed = passwordEncoder.encode(pin);
        log.debug("PIN hashed successfully");
        return hashed;
    }

    /**
     * Verify PIN against stored hash
     */
    public boolean verifyPin(String rawPin, String hashedPin) {
        boolean isValid = passwordEncoder.matches(rawPin, hashedPin);
        log.debug("PIN verification: {}", isValid);
        return isValid;
    }

    /**
     * Generate random CVV
     */
    public String generateRandomCvv() {
        int cvv = 100 + secureRandom.nextInt(900);
        return String.valueOf(cvv);
    }

    /**
     * Generate random PIN (user will set this during activation)
     */
    public String generateRandomPin() {
        int pin = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(pin);
    }
}
