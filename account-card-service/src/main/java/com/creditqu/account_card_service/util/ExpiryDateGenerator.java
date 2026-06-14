package com.creditqu.account_card_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ExpiryDateGenerator {

    @Value("${account-card.card.expiry-years:5}")
    private int expiryYears;

    /**
     * Generate expiry month and year based on current date
     * Returns MMYY format
     */
    public String generateExpiryDateMMYY() {
        YearMonth expiry = YearMonth.now().plusYears(expiryYears);
        String expiryStr = expiry.format(DateTimeFormatter.ofPattern("MMyy"));
        log.debug("Generated expiry date: {}", expiryStr);
        return expiryStr;
    }

    /**
     * Generate expiry month and year as separate values
     */
    public ExpiryDate generateExpiryDate() {
        YearMonth expiry = YearMonth.now().plusYears(expiryYears);
        return ExpiryDate.builder()
                .month(expiry.getMonthValue())
                .year(expiry.getYear())
                .build();
    }

    /**
     * Check if card is expired
     */
    public boolean isExpired(int expiryMonth, int expiryYear) {
        YearMonth cardExpiry = YearMonth.of(expiryYear, expiryMonth);
        YearMonth now = YearMonth.now();
        boolean expired = cardExpiry.isBefore(now);
        if (expired) {
            log.debug("Card expired: {}/{}", expiryMonth, expiryYear);
        }
        return expired;
    }

    @lombok.Builder
    @lombok.Data
    public static class ExpiryDate {
        private int month;
        private int year;
    }
}
