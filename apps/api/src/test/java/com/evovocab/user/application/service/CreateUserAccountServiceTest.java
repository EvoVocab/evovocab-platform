package com.evovocab.user.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.evovocab.user.application.port.in.CreateUserAccountCommand;
import com.evovocab.user.application.port.out.PasswordEncoderPort;
import com.evovocab.user.domain.exception.EmailAlreadyExistsException;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.repository.UserAccountRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateUserAccountServiceTest {

    private static final String MOCK_HASHED_PASSWORD = "$2b$10$" + "a".repeat(53);

    private UserAccountRepository accountRepository;
    private PasswordEncoderPort passwordEncoder;
    private CreateUserAccountService service;

    @BeforeEach
    void setUp() {
        accountRepository = mock(UserAccountRepository.class);
        passwordEncoder = mock(PasswordEncoderPort.class);
        service = new CreateUserAccountService(accountRepository, passwordEncoder);
    }

    @Test
    void createsUserAccountWhenEmailIsAvailableAndRegistrationDataIsValid() {
        CreateUserAccountCommand command = validCommand();
        when(accountRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(command.password())).thenReturn(MOCK_HASHED_PASSWORD);
        Instant before = Instant.now();

        service.execute(command);

        Instant after = Instant.now();
        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        // verify check duoc goi it nhat 1 lan
        verify(accountRepository).findByEmail(command.email());
        verify(passwordEncoder).encode(command.password());
        verify(accountRepository).save(accountCaptor.capture()); // bat lai account thuc te duoc truyen vao ham save
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
            () -> assertEquals(account.getUpdatedAt(), account.getProfile().getUpdatedAt())
        );
    }

    @Test
    void rejectsUserAccountWhenEmailAlreadyExists() {
        CreateUserAccountCommand command = validCommand();
        when(accountRepository.findByEmail(command.email())).thenReturn(Optional.of(mock(UserAccount.class)));

        assertThrows(EmailAlreadyExistsException.class, () -> service.execute(command));

        verify(accountRepository).findByEmail(command.email());
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void rejectsUserAccountWhenStatusIsActive() {
        CreateUserAccountCommand command = new CreateUserAccountCommand(
            "Nguyen Van A",
            "account@example.com",
            "TestPassword1!",
            UserAccountStatus.ACTIVE,
            UserAccountProvider.EMAIL
        );
        // Email chua ton tai
        when(accountRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));

        verify(accountRepository).findByEmail(command.email());
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void rejectsUserAccountWhenEncoderReturnsInvalidHash() {
        CreateUserAccountCommand command = validCommand();
        when(accountRepository.findByEmail(command.email())).thenReturn(Optional.empty());
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
