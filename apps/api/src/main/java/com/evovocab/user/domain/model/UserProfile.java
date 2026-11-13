package com.evovocab.user.domain.model;

import java.time.Instant;

public class UserProfile {

    private final UserAccountId accountId;
    private String fullName;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserProfile(UserAccountId accountId, String fullName, Instant createdAt, Instant updatedAt) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserAccountId getAccountId() {
        return accountId;
    }

    public String getFullName() {
        return fullName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
