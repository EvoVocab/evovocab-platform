package com.evovocab.user.infrastructure.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.mapper.UserAccountMapperImpl;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({ UserAccountRepositoryImpl.class, UserAccountMapperImpl.class })
class UserAccountRepositoryImplTest {

    @Autowired
    private UserAccountRepositoryImpl repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveThenFindByIdLoadsAccountAndProfileAsDomainModels() {
        UUID accountId = UUID.randomUUID();
        repository.save(newAccount(accountId));
        entityManager.flush();
        entityManager.clear();

        UserAccount loaded = repository.findById(accountId).orElseThrow();

        assertLoadedAccount(loaded, accountId);
    }

    @Test
    void saveThenFindByEmailLoadsAccountAndProfileAsDomainModels() {
        UUID accountId = UUID.randomUUID();
        repository.save(newAccount(accountId));
        entityManager.flush();
        entityManager.clear();

        UserAccount loaded = repository.findByEmail("account@example.com").orElseThrow();

        assertLoadedAccount(loaded, accountId);
    }

    private UserAccount newAccount(UUID accountId) {
        UserAccount account = new UserAccount(
            accountId,
            "account@example.com",
            "hashed-password",
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL,
            null,
            null,
            null,
            null
        );
        account.setProfile(new UserProfile(accountId, "Test User", null, null));
        return account;
    }

    private void assertLoadedAccount(UserAccount loaded, UUID accountId) {
        assertAll(
            () -> assertEquals(accountId, loaded.getId()),
            () -> assertEquals("account@example.com", loaded.getEmail()),
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
