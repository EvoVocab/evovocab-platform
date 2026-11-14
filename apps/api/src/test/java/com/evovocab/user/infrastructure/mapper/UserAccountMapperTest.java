package com.evovocab.user.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

class UserAccountMapperTest {

    private static final String VALID_HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode("TestPassword1!");
    private static final String INVALID_HASH = "plain-text-not-a-bcrypt-hash";

    private static final Instant CREATED_AT = Instant.parse("2026-11-01T10:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-11-02T10:00:00Z");
    private static final Instant DELETED_AT = Instant.parse("2026-11-03T10:00:00Z");

    private final UserAccountMapper mapper = Mappers.getMapper(UserAccountMapper.class);

    @Test
    void mapsFullDomainAccountToJpaEntity() {
        UserAccountId id = UserAccountId.generate();
        UserProfile profile = new UserProfile(id, "Nguyen Van A", CREATED_AT, UPDATED_AT);
        UserAccount account = new UserAccount(
            id,
            "user@example.com",
            Password.fromHash(VALID_HASH),
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL,
            "",
            CREATED_AT,
            UPDATED_AT,
            DELETED_AT,
            profile
        );

        UserAccountEntity entity = mapper.toJpaEntity(account);

        assertAll(
            () -> assertNotNull(entity),
            () -> assertEquals(id.getValue(), entity.getId()),
            () -> assertEquals(account.getEmail(), entity.getEmail()),
            () -> assertEquals(account.getPassword().getHashedValue(), entity.getPassword()),
            () -> assertEquals(account.getStatus(), entity.getStatus()),
            () -> assertEquals(account.getProvider(), entity.getProvider()),
            () -> assertEquals(account.getProviderId(), entity.getProviderId()),
            () -> assertEquals(account.getCreatedAt(), entity.getLifecycleMetadata().getCreatedAt()),
            () -> assertEquals(account.getUpdatedAt(), entity.getLifecycleMetadata().getUpdatedAt()),
            () -> assertEquals(account.getDeletedAt(), entity.getLifecycleMetadata().getDeletedAt()),
            () -> assertNotNull(entity.getProfile()),
            () -> assertEquals(account.getProfile().getFullName(), entity.getProfile().getFullName()),
            () -> assertEquals(account.getCreatedAt(), entity.getProfile().getAuditMetadata().getCreatedAt()),
            () -> assertEquals(account.getUpdatedAt(), entity.getProfile().getAuditMetadata().getUpdatedAt()),
            () -> assertSame(entity, entity.getProfile().getAccount())
        );
    }

    @Test
    void mapsFullJpaEntityToDomainAccount() {
        UserAccountId id = UserAccountId.generate();

        LifecycleMetadata lifecycle = new LifecycleMetadata();
        lifecycle.setCreatedAt(CREATED_AT);
        lifecycle.setUpdatedAt(UPDATED_AT);
        lifecycle.setDeletedAt(DELETED_AT);

        AuditMetadata audit = new AuditMetadata();
        audit.setCreatedAt(CREATED_AT);
        audit.setUpdatedAt(UPDATED_AT);

        UserProfileEntity profileEntity = new UserProfileEntity();
        profileEntity.setId(id.getValue());
        profileEntity.setFullName("Nguyen Van A");
        profileEntity.setAuditMetadata(audit);

        UserAccountEntity entity = new UserAccountEntity();
        entity.setId(id.getValue());
        entity.setEmail("user@example.com");
        entity.setPassword(VALID_HASH);
        entity.setStatus(UserAccountStatus.INACTIVE);
        entity.setProvider(UserAccountProvider.EMAIL);
        entity.setProviderId("");
        entity.setLifecycleMetadata(lifecycle);
        entity.setProfile(profileEntity);

        UserAccount account = mapper.toDomainEntity(entity);

        assertAll(
            () -> assertNotNull(account),
            () -> assertEquals(id, account.getId()),
            () -> assertEquals(entity.getEmail(), account.getEmail()),
            () -> assertEquals(entity.getPassword(), account.getPassword().getHashedValue()),
            () -> assertEquals(entity.getStatus(), account.getStatus()),
            () -> assertEquals(entity.getProvider(), account.getProvider()),
            () -> assertEquals(entity.getProviderId(), account.getProviderId()),
            () -> assertEquals(entity.getLifecycleMetadata().getCreatedAt(), account.getCreatedAt()),
            () -> assertEquals(entity.getLifecycleMetadata().getUpdatedAt(), account.getUpdatedAt()),
            () -> assertEquals(entity.getLifecycleMetadata().getDeletedAt(), account.getDeletedAt()),
            () -> assertNotNull(account.getProfile()),
            () -> assertEquals(id, account.getProfile().getAccountId()),
            () -> assertEquals(entity.getProfile().getFullName(), account.getProfile().getFullName()),
            () -> assertEquals(entity.getLifecycleMetadata().getCreatedAt(), account.getProfile().getCreatedAt()),
            () -> assertEquals(entity.getLifecycleMetadata().getUpdatedAt(), account.getProfile().getUpdatedAt())
        );
    }

    @Test
    void mapsDomainAccountWithNullOptionalFieldsToJpaEntity() {
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

        assertAll(
            () -> assertNotNull(entity),
            () -> assertEquals(id.getValue(), entity.getId()),
            () -> assertEquals(account.getEmail(), entity.getEmail()),
            () -> assertNull(entity.getPassword()),
            () -> assertNull(entity.getProviderId()),
            () -> assertNull(entity.getProfile())
        );
    }

    @Test
    void mapsJpaEntityWithNullOptionalFieldsToDomainAccount() {
        UserAccountId id = UserAccountId.generate();
        UserAccountEntity entity = new UserAccountEntity();
        entity.setId(id.getValue());
        entity.setEmail("user@example.com");
        entity.setStatus(UserAccountStatus.INACTIVE);
        entity.setProvider(UserAccountProvider.EMAIL);

        UserAccount account = mapper.toDomainEntity(entity);

        assertAll(
            () -> assertNotNull(account),
            () -> assertEquals(id, account.getId()),
            () -> assertEquals(entity.getEmail(), account.getEmail()),
            () -> assertNull(account.getPassword()),
            () -> assertNull(account.getProviderId()),
            () -> assertNull(account.getProfile()),
            () -> assertNull(account.getCreatedAt()),
            () -> assertNull(account.getUpdatedAt()),
            () -> assertNull(account.getDeletedAt())
        );
    }

    @Test
    void mapsStandaloneProfileToJpaEntity() {
        UserAccountId id = UserAccountId.generate();
        UserProfile profile = new UserProfile(id, "Nguyen Van A", CREATED_AT, UPDATED_AT);

        UserProfileEntity entity = mapper.toJpaEntity(profile);

        assertAll(
            () -> assertNotNull(entity),
            () -> assertEquals(profile.getFullName(), entity.getFullName()),
            () -> assertEquals(profile.getCreatedAt(), entity.getAuditMetadata().getCreatedAt()),
            () -> assertEquals(profile.getUpdatedAt(), entity.getAuditMetadata().getUpdatedAt()),
            () -> assertNull(entity.getId()),
            () -> assertNull(entity.getAccount())
        );
    }

    @Test
    void mapsStandaloneProfileEntityToDomain() {
        UserAccountId id = UserAccountId.generate();
        AuditMetadata audit = new AuditMetadata();
        audit.setCreatedAt(CREATED_AT);
        audit.setUpdatedAt(UPDATED_AT);

        UserProfileEntity entity = new UserProfileEntity();
        entity.setId(id.getValue());
        entity.setFullName("Nguyen Van A");
        entity.setAuditMetadata(audit);

        UserProfile profile = mapper.toDomainEntity(entity);

        assertAll(
            () -> assertNotNull(profile),
            () -> assertEquals(id, profile.getAccountId()),
            () -> assertEquals(entity.getFullName(), profile.getFullName()),
            () -> assertEquals(entity.getAuditMetadata().getCreatedAt(), profile.getCreatedAt()),
            () -> assertEquals(entity.getAuditMetadata().getUpdatedAt(), profile.getUpdatedAt())
        );
    }

    @Test
    void returnsNullWhenMappingNullInputs() {
        assertAll(
            () -> assertNull(mapper.toJpaEntity((UserAccount) null)),
            () -> assertNull(mapper.toDomainEntity((UserAccountEntity) null)),
            () -> assertNull(mapper.toJpaEntity((UserProfile) null)),
            () -> assertNull(mapper.toDomainEntity((UserProfileEntity) null)),
            () -> assertNull(mapper.toJpaId(null)),
            () -> assertNull(mapper.toDomainId(null)),
            () -> assertNull(mapper.toJpaPassword(null)),
            () -> assertNull(mapper.toDomainPassword(null))
        );
    }

    @Test
    void mapsEmptyPasswordToJpaString() {
        assertEquals("", mapper.toJpaPassword(Password.getEmptyPassword()));
    }

    @Test
    void throwsWhenMappingInvalidPasswordHashToDomain() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> mapper.toDomainPassword("")),
            () -> assertThrows(IllegalArgumentException.class, () -> mapper.toDomainPassword("   ")),
            () -> assertThrows(IllegalArgumentException.class, () -> mapper.toDomainPassword(INVALID_HASH))
        );
    }
}
