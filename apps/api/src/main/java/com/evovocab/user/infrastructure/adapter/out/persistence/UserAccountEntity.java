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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
@NoArgsConstructor
public class UserAccountEntity extends IdUUID {

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", length = 60)
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
}
