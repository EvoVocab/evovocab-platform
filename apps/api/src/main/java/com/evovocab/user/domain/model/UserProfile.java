package com.evovocab.user.domain.model;

import java.time.Instant;
import java.util.UUID;

public class UserProfile {

    private final UUID accountId;
    private String fullName;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserProfile(UUID accountId, String fullName, Instant createdAt, Instant updatedAt) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getAccountId() {
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
