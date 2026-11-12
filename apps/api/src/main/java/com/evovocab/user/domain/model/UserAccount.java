package com.evovocab.user.domain.model;

import java.time.Instant;

public class UserAccount {

    private final UserAccountId id;
    private String email;
    private Password password;
    private UserAccountStatus status;
    private UserAccountProvider provider;
    private String providerId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private UserProfile profile;

    public UserAccount(
        UserAccountId id,
        String email,
        Password password,
        UserAccountStatus status,
        UserAccountProvider provider,
        String providerId,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        UserProfile profile
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.profile = profile;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public UserAccountId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public UserAccountStatus getStatus() {
        return status;
    }

    public UserAccountProvider getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setStatus(UserAccountStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
