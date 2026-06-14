package com.creditqu.common_module.constant;

public enum AccountStatus {
    ACTIVE("Active - Can perform transactions"),
    BLOCKED("Blocked - Temporarily suspended"),
    CLOSED("Closed - Permanently closed"),
    SUSPENDED("Suspended - Under investigation");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
