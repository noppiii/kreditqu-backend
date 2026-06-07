package com.creditqu.application_service.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApplicationNumberGenerator {

    private static final String PREFIX = "APP";
    private static final AtomicInteger counter = new AtomicInteger(1);

    public synchronized String generate() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String sequence = String.format("%04d", counter.getAndIncrement() % 10000);
        return PREFIX + dateTime + sequence;
    }
}
