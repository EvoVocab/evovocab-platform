package com.evovocab.user.infrastructure.adapter.out.persistence;

import com.evovocab.shared.entities.IdUUID;
import com.evovocab.shared.entities.LifecycleMetadata;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccountEntity extends IdUUID {

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserAccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 50)
    private UserAccountProvider provider;

    @Column(name = "provider_id", length = 255)
    private String providerId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "createdAt",
            column = @Column(name = "created_at", nullable = false, updatable = false)
        ),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at", nullable = false)),
        @AttributeOverride(name = "deletedAt", column = @Column(name = "deleted_at"))
    })
    private LifecycleMetadata lifecycleMetadata;

    @OneToOne(mappedBy = "account", cascade = { CascadeType.MERGE }, orphanRemoval = true)
    private UserProfileEntity profile;

    public UserAccountEntity() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAccountStatus getStatus() {
        return status;
    }

    public void setStatus(UserAccountStatus status) {
        this.status = status;
    }

    public UserAccountProvider getProvider() {
        return provider;
    }

    public void setProvider(UserAccountProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public LifecycleMetadata getLifecycleMetadata() {
        return lifecycleMetadata;
    }

    public void setLifecycleMetadata(LifecycleMetadata lifecycleMetadata) {
        this.lifecycleMetadata = lifecycleMetadata;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(UserProfileEntity profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setAccount(this);
        }
    }
}
