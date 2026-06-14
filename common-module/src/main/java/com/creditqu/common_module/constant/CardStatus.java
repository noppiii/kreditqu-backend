package com.creditqu.common_module.constant;

public enum CardStatus {
    INACTIVE("Issued but not activated"),
    ACTIVE("Active - Ready to use"),
    BLOCKED("Blocked - Temporarily suspended"),
    LOST("Lost - Reported lost"),
    STOLEN("Stolen - Reported stolen"),
    EXPIRED("Expired - Past expiry date"),
    CLOSED("Closed - Account closed");

    private final String description;

    CardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
