package com.evovocab.user.infrastructure.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.evovocab.user.domain.model.Password;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.mapper.UserAccountMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({ UserAccountRepositoryImpl.class, UserAccountMapperImpl.class })
class UserAccountRepositoryImplTest {

    private static final String HASHED_PASSWORD = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode(
        "TestPassword1!"
    );

    @Autowired
    private UserAccountRepositoryImpl repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void loadsAccountAndProfileByIdWhenSaved() {
        UserAccountId accountId = UserAccountId.generate();
        repository.save(newAccount(accountId));
        entityManager.flush();
        entityManager.clear();

        UserAccount loaded = repository.findById(accountId).orElseThrow();

        assertLoadedAccount(loaded, accountId);
    }

    @Test
    void loadsAccountAndProfileByEmailWhenSaved() {
        UserAccountId accountId = UserAccountId.generate();
        repository.save(newAccount(accountId));
        entityManager.flush();
        entityManager.clear();

        UserAccount loaded = repository.findByEmail("account@example.com").orElseThrow();

        assertLoadedAccount(loaded, accountId);
    }

    @Test
    void returnsEmptyByIdWhenAccountMissing() {
        assertTrue(repository.findById(UserAccountId.generate()).isEmpty());
    }

    @Test
    void returnsEmptyByEmailWhenAccountMissing() {
        assertTrue(repository.findByEmail("missing@example.com").isEmpty());
    }

    @Test
    void returnsTrueWhenEmailExists() {
        repository.save(newAccount(UserAccountId.generate()));
        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.existsByEmail("account@example.com"));
    }

    @Test
    void returnsFalseWhenEmailDoesNotExist() {
        assertFalse(repository.existsByEmail("missing@example.com"));
    }

    private UserAccount newAccount(UserAccountId accountId) {
        return new UserAccount(
            accountId,
            "account@example.com",
            Password.fromHash(HASHED_PASSWORD),
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL,
            null,
            null,
            null,
            null,
            new UserProfile(accountId, "Test User", null, null)
        );
    }

    private void assertLoadedAccount(UserAccount loaded, UserAccountId accountId) {
        assertAll(
            () -> assertEquals(accountId, loaded.getId()),
            () -> assertEquals("account@example.com", loaded.getEmail()),
            () -> assertEquals(HASHED_PASSWORD, loaded.getPassword().getHashedValue()),
            () -> assertEquals(UserAccountStatus.INACTIVE, loaded.getStatus()),
            () -> assertEquals(UserAccountProvider.EMAIL, loaded.getProvider()),
            () -> assertNotNull(loaded.getProfile()),
            () -> assertEquals(accountId, loaded.getProfile().getAccountId()),
            () -> assertEquals("Test User", loaded.getProfile().getFullName()),
            () -> assertNotNull(loaded.getCreatedAt()),
            () -> assertNotNull(loaded.getProfile().getCreatedAt())
        );
    }
}
