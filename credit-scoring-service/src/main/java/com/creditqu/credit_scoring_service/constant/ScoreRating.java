package com.creditqu.credit_scoring_service.constant;

public enum ScoreRating {
    VERY_POOR("Very Poor", 0, 349),
    POOR("Poor", 350, 499),
    FAIR("Fair", 500, 599),
    GOOD("Good", 600, 749),
    VERY_GOOD("Very Good", 750, 799),
    EXCELLENT("Excellent", 800, 850);

    private final String description;
    private final int minScore;
    private final int maxScore;

    ScoreRating(String description, int minScore, int maxScore) {
        this.description = description;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public static ScoreRating fromScore(int score) {
        for (ScoreRating rating : values()) {
            if (score >= rating.minScore && score <= rating.maxScore) {
                return rating;
            }
        }
        return POOR;
    }

    public String getDescription() {
        return description;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }
}
