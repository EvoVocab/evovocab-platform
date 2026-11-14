package com.evovocab.user.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.evovocab.shared.entities.AuditMetadata;
import com.evovocab.shared.entities.LifecycleMetadata;
import com.evovocab.user.domain.model.Password;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserAccountEntity;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserProfileEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserAccountMapperTest {

    private static final String HASH = "$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZabcde";
    private static final Instant CREATED_AT = Instant.parse("2026-11-01T10:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-11-02T10:00:00Z");
    private static final Instant DELETED_AT = Instant.parse("2026-11-03T10:00:00Z");

    private final UserAccountMapper mapper = Mappers.getMapper(UserAccountMapper.class);

    @Test
    void mapsAccountAndProfileToJpaEntity() {
        UserAccountId id = UserAccountId.generate();
        UserProfile profile = new UserProfile(id, "Nguyen Van A", CREATED_AT, UPDATED_AT);
        UserAccount account = new UserAccount(
            id,
            "user@example.com",
            Password.hashForPersistence(HASH),
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL,
            "external-id",
            CREATED_AT,
            UPDATED_AT,
            DELETED_AT,
            profile
        );

        UserAccountEntity entity = mapper.toJpaEntity(account);

        assertAll(
            () -> assertEquals(id.getValue(), entity.getId()),
            () -> assertEquals(account.getEmail(), entity.getEmail()),
            () -> assertEquals(HASH, entity.getPassword()),
            () -> assertEquals(account.getStatus(), entity.getStatus()),
            () -> assertEquals(account.getProvider(), entity.getProvider()),
            () -> assertEquals(account.getProviderId(), entity.getProviderId()),
            () -> assertEquals(CREATED_AT, entity.getLifecycleMetadata().getCreatedAt()),
            () -> assertEquals(UPDATED_AT, entity.getLifecycleMetadata().getUpdatedAt()),
            () -> assertEquals(DELETED_AT, entity.getLifecycleMetadata().getDeletedAt()),
            () -> assertEquals(profile.getFullName(), entity.getProfile().getFullName()),
            () -> assertEquals(CREATED_AT, entity.getProfile().getAuditMetadata().getCreatedAt()),
            () -> assertEquals(UPDATED_AT, entity.getProfile().getAuditMetadata().getUpdatedAt()),
            () -> assertSame(entity, entity.getProfile().getAccount())
        );
    }

    @Test
    void mapsJpaEntityAndProfileToDomain() {
        UserAccountId id = UserAccountId.generate();
        UserAccountEntity entity = new UserAccountEntity();
        entity.setId(id.getValue());
        entity.setEmail("user@example.com");
        entity.setPassword(HASH);
        entity.setStatus(UserAccountStatus.INACTIVE);
        entity.setProvider(UserAccountProvider.EMAIL);
        entity.setProviderId("external-id");
        LifecycleMetadata lifecycle = new LifecycleMetadata();
        lifecycle.setCreatedAt(CREATED_AT);
        lifecycle.setUpdatedAt(UPDATED_AT);
        lifecycle.setDeletedAt(DELETED_AT);
        entity.setLifecycleMetadata(lifecycle);
        UserProfileEntity profile = new UserProfileEntity();
        profile.setId(id.getValue());
        profile.setFullName("Nguyen Van A");
        AuditMetadata audit = new AuditMetadata();
        audit.setCreatedAt(CREATED_AT);
        audit.setUpdatedAt(UPDATED_AT);
        profile.setAuditMetadata(audit);
        entity.setProfile(profile);

        UserAccount account = mapper.toDomainEntity(entity);

        assertAll(
            () -> assertEquals(id, account.getId()),
            () -> assertEquals(entity.getEmail(), account.getEmail()),
            () -> assertEquals(HASH, account.getPassword().getHashedValue()),
            () -> assertEquals(entity.getStatus(), account.getStatus()),
            () -> assertEquals(entity.getProvider(), account.getProvider()),
            () -> assertEquals(entity.getProviderId(), account.getProviderId()),
            () -> assertEquals(CREATED_AT, account.getCreatedAt()),
            () -> assertEquals(UPDATED_AT, account.getUpdatedAt()),
            () -> assertEquals(DELETED_AT, account.getDeletedAt()),
            () -> assertEquals(id, account.getProfile().getAccountId()),
            () -> assertEquals(profile.getFullName(), account.getProfile().getFullName()),
            () -> assertEquals(CREATED_AT, account.getProfile().getCreatedAt()),
            () -> assertEquals(UPDATED_AT, account.getProfile().getUpdatedAt())
        );
    }

    @Test
    void mapsOptionalFieldsAndMissingProfile() {
        UserAccountId id = UserAccountId.generate();
        UserAccount account = new UserAccount(
            id,
            "user@example.com",
            null,
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL,
            null,
            null,
            null,
            null,
            null
        );

        UserAccountEntity entity = mapper.toJpaEntity(account);
        UserAccount restored = mapper.toDomainEntity(entity);

        assertAll(
            () -> assertEquals(id, restored.getId()),
            () -> assertNull(entity.getPassword()),
            () -> assertNull(entity.getProviderId()),
            () -> assertNull(entity.getProfile()),
            () -> assertNull(restored.getPassword()),
            () -> assertNull(restored.getProfile()),
            () -> assertNull(restored.getCreatedAt()),
            () -> assertNull(restored.getUpdatedAt()),
            () -> assertNull(restored.getDeletedAt())
        );
    }

    @Test
    void mapsStandaloneProfileWithoutAccountReference() {
        UserAccountId id = UserAccountId.generate();
        UserProfile profile = new UserProfile(id, "Nguyen Van A", CREATED_AT, UPDATED_AT);

        UserProfileEntity entity = mapper.toJpaEntity(profile);

        assertAll(
            () -> assertEquals(profile.getFullName(), entity.getFullName()),
            () -> assertEquals(CREATED_AT, entity.getAuditMetadata().getCreatedAt()),
            () -> assertEquals(UPDATED_AT, entity.getAuditMetadata().getUpdatedAt()),
            () -> assertNull(entity.getId()),
            () -> assertNull(entity.getAccount())
        );
        entity.setId(id.getValue());
        assertEquals(id, mapper.toDomainEntity(entity).getAccountId());
    }

    @Test
    void mapsNullInputsAndRejectsEmptyOrBcrypt2bCost10Password() {
        assertAll(
            () -> assertNull(mapper.toJpaEntity((UserAccount) null)),
            () -> assertNull(mapper.toDomainEntity((UserAccountEntity) null)),
            () -> assertNull(mapper.toJpaEntity((UserProfile) null)),
            () -> assertNull(mapper.toDomainEntity((UserProfileEntity) null)),
            () -> assertNull(mapper.toJpaId(null)),
            () -> assertNull(mapper.toDomainId(null)),
            () -> assertNull(mapper.toJpaPassword(null)),
            () -> assertNull(mapper.toDomainPassword(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> mapper.toDomainPassword("")),
            () ->
                assertThrows(IllegalArgumentException.class, () ->
                    mapper.toDomainPassword(HASH.replace("$2a$", "$2b$"))
                )
        );
    }
}
