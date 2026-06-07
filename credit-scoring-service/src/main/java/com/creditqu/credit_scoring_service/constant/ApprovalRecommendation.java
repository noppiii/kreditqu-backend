package com.creditqu.credit_scoring_service.constant;

public enum ApprovalRecommendation {
    APPROVE("Automatically Approved"),
    REJECT("Automatically Rejected"),
    MANUAL_REVIEW("Requires Manual Review");

    private final String description;

    ApprovalRecommendation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}