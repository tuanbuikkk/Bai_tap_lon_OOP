package com.yourteam.englishapp.model;

/** Mức truy cập nội dung. Cấp cao hơn truy cập được cấp thấp hơn. */
public enum AccessLevel {
    FREE(0), PREMIUM(1);

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public boolean canAccess(AccessLevel required) {
        return this.level >= required.level;
    }
}
