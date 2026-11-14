package com.evovocab.user.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.evovocab.user.application.port.in.CreateUserAccountCommand;
import com.evovocab.user.application.port.out.UserResult;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.repository.UserAccountRepository;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

class CreateUserAccountServiceTest {

    private static final String MOCK_HASHED_PASSWORD = "$2b$10$" + "a".repeat(53);

    private UserAccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private CreateUserAccountService service;

    @BeforeEach
    void setUp() {
        accountRepository = mock(UserAccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new CreateUserAccountService(accountRepository, passwordEncoder);
    }

    @Test
    void encodesPasswordAndReturnsCreatedUserAccount() {
        CreateUserAccountCommand command = validCommand();
        when(passwordEncoder.encode(command.password())).thenReturn(MOCK_HASHED_PASSWORD);
        when(accountRepository.save(any(UserAccount.class))).thenAnswer((invocation) -> invocation.getArgument(0));
        Instant before = Instant.now();

        UserResult result = service.execute(command);

        Instant after = Instant.now();
        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        verify(passwordEncoder).encode(command.password());
        verify(accountRepository).save(accountCaptor.capture());
        UserAccount account = accountCaptor.getValue();
        assertAll(
            () -> assertNotNull(account.getId()),
            () -> assertEquals(command.email(), account.getEmail()),
            () -> assertEquals(MOCK_HASHED_PASSWORD, account.getPassword().getHashedValue()),
            () -> assertEquals(UserAccountStatus.INACTIVE, account.getStatus()),
            () -> assertEquals(UserAccountProvider.EMAIL, account.getProvider()),
            () -> assertNull(account.getProviderId()),
            () -> assertNull(account.getDeletedAt()),
            () -> assertFalse(account.getCreatedAt().isBefore(before)),
            () -> assertFalse(account.getCreatedAt().isAfter(after)),
            () -> assertEquals(account.getCreatedAt(), account.getUpdatedAt()),
            () -> assertNotNull(account.getProfile()),
            () -> assertEquals(account.getId(), account.getProfile().getAccountId()),
            () -> assertEquals(command.fullName(), account.getProfile().getFullName()),
            () -> assertEquals(account.getCreatedAt(), account.getProfile().getCreatedAt()),
            () -> assertEquals(account.getUpdatedAt(), account.getProfile().getUpdatedAt()),
            () -> assertEquals(account.getId().getValue(), result.id()),
            () -> assertEquals(account.getEmail(), result.email()),
            () -> assertEquals(account.getStatus().name(), result.status()),
            () -> assertEquals(account.getProvider().name(), result.provider())
        );
    }

    @Test
    void doesNotSaveAccountWhenPasswordEncoderFails() {
        CreateUserAccountCommand command = validCommand();
        IllegalArgumentException failure = new IllegalArgumentException("Password cannot be encoded");
        when(passwordEncoder.encode(command.password())).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> service.execute(command)));

        verify(passwordEncoder).encode(command.password());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void returnsResultFromAccountReturnedByRepository() {
        CreateUserAccountCommand command = validCommand();
        UserAccount savedAccount = new UserAccount(
            UserAccountId.generate(),
            "saved@example.com",
            null,
            UserAccountStatus.ACTIVE,
            UserAccountProvider.EMAIL,
            null,
            null,
            null,
            null,
            null
        );
        when(passwordEncoder.encode(command.password())).thenReturn(MOCK_HASHED_PASSWORD);
        when(accountRepository.save(any(UserAccount.class))).thenReturn(savedAccount);

        UserResult result = service.execute(command);

        assertEquals(
            new UserResult(
                savedAccount.getId().getValue(),
                savedAccount.getEmail(),
                savedAccount.getStatus().name(),
                savedAccount.getProvider().name()
            ),
            result
        );
    }

    @Test
    void rejectsUserAccountWhenEncoderReturnsInvalidHash() {
        CreateUserAccountCommand command = validCommand();
        when(passwordEncoder.encode(command.password())).thenReturn("plain-text");

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));

        verify(passwordEncoder).encode(command.password());
        verify(accountRepository, never()).save(any());
    }

    private CreateUserAccountCommand validCommand() {
        return new CreateUserAccountCommand(
            "Nguyen Van A",
            "account@example.com",
            "TestPassword1!",
            UserAccountStatus.INACTIVE,
            UserAccountProvider.EMAIL
        );
    }
}
