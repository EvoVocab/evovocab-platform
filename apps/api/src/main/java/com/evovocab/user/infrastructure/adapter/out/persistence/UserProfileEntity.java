package com.evovocab.user.infrastructure.adapter.out.persistence;

import com.evovocab.shared.entities.AuditMetadata;
import com.evovocab.shared.entities.IdUUID;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfileEntity extends IdUUID {

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private UserAccountEntity account;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "createdAt",
            column = @Column(name = "created_at", nullable = false, updatable = false)
        ),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at", nullable = false))
    })
    private AuditMetadata auditMetadata;
}
