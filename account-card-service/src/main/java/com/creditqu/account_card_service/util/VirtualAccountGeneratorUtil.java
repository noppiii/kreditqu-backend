package com.creditqu.account_card_service.util;

import com.creditqu.account_card_service.repository.VirtualAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class VirtualAccountGeneratorUtil {

    private final VirtualAccountRepository virtualAccountRepository;

    @Value("${account-card.virtual-account.bank-codes}")
    private List<String> bankCodes;

    @Value("${account-card.virtual-account.expiry-days:30}")
    private int expiryDays;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a unique virtual account number
     */
    public String generateVirtualAccountNumber(Long customerId, Long accountId, String bankCode) {
        // Format: BANK_CODE + CUSTOMER_ID (10 digits) + ACCOUNT_ID (10 digits) + CHECKSUM
        String customerIdPadded = String.format("%010d", customerId);
        String accountIdPadded = String.format("%010d", accountId);
        String base = bankCode + customerIdPadded + accountIdPadded;

        // Calculate simple checksum
        int checksum = calculateChecksum(base);
        String vaNumber = base + checksum;

        log.debug("Generated VA number: {} for bank: {}", vaNumber, bankCode);
        return vaNumber;
    }

    /**
     * Get random bank code for VA
     */
    public String getRandomBankCode() {
        String bankCode = bankCodes.get(secureRandom.nextInt(bankCodes.size()));
        log.debug("Selected bank code: {}", bankCode);
        return bankCode;
    }

    /**
     * Calculate expiry date for VA
     */
    public LocalDateTime calculateExpiryDate() {
        LocalDateTime expiry = LocalDateTime.now().plusDays(expiryDays);
        log.debug("VA expiry date: {}", expiry);
        return expiry;
    }

    private int calculateChecksum(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            sum += input.charAt(i);
        }
        return sum % 10;
    }
}