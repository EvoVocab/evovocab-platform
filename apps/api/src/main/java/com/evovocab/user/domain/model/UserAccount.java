package com.evovocab.user.domain.model;

import java.time.Instant;
import java.util.UUID;

public class UserAccount {

    private final UUID id;
    private String email;
    private String password;
    private UserAccountStatus status;
    private UserAccountProvider provider;
    private String providerId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public UserAccount(
        UUID id,
        String email,
        String password,
        UserAccountStatus status,
        UserAccountProvider provider,
        String providerId,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
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
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(UserAccountStatus status) {
        this.status = status;
    }

    public void setProvider(UserAccountProvider provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
