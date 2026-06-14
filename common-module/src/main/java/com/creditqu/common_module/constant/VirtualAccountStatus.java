package com.creditqu.common_module.constant;

public enum VirtualAccountStatus {
    ACTIVE("Active - Can be used for payment"),
    EXPIRED("Expired - Past expiry date"),
    USED("Used - Payment completed");

    private final String description;

    VirtualAccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
