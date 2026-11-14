package com.evovocab.user.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.evovocab.user.application.port.in.CreateUserByEmailCommand;
import com.evovocab.user.application.port.out.UserResult;
import com.evovocab.user.domain.exception.EmailAlreadyExistsException;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.domain.repository.UserAccountRepository;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

class CreateUserAccountServiceTest {

    private static final String VALID_HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode("TestPassword1!");

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
        CreateUserByEmailCommand command = validCommand();

        when(accountRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenReturn(VALID_HASH);
        // Tra ve chinh xac thang UserAccount duoc goi voi save
        when(accountRepository.save(any(UserAccount.class))).thenAnswer((invocation) -> invocation.getArgument(0));

        Instant before = Instant.now();
        UserResult result = service.executeForEmail(command);
        Instant after = Instant.now();
        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        InOrder order = inOrder(accountRepository, passwordEncoder);
        // Dung order de chi dinh chay accountRepository truoc
        order.verify(accountRepository).existsByEmail(command.email());
        order.verify(passwordEncoder).encode(command.password());
        order.verify(accountRepository).save(accountCaptor.capture());

        UserAccount account = accountCaptor.getValue(); // Bat lai thang account da duoc luu

        assertAll(
            () -> assertNotNull(account.getId()),
            () -> assertEquals(command.email(), account.getEmail()),
            () -> assertEquals(VALID_HASH, account.getPassword().getHashedValue()),
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
            () -> assertEquals(account.getProfile().getFullName(), result.fullName()),
            () -> assertEquals(account.getEmail(), result.email()),
            () -> assertEquals(account.getStatus().name(), result.status()),
            () -> assertEquals(account.getProvider().name(), result.provider())
        );
    }

    @Test
    void returnsResultMappedFromSavedAccountReturnedByRepository() {
        CreateUserByEmailCommand command = validCommand();
        UserAccountId savedId = UserAccountId.generate();
        UserAccount savedAccount = new UserAccount(
            savedId,
            "saved@example.com",
            null,
            UserAccountStatus.ACTIVE,
            UserAccountProvider.EMAIL,
            null,
            null,
            null,
            null,
            new UserProfile(savedId, "Saved User", null, null)
        );

        when(accountRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenReturn(VALID_HASH);
        when(accountRepository.save(any(UserAccount.class))).thenReturn(savedAccount);

        UserResult result = service.executeForEmail(command);

        assertEquals(
            new UserResult(
                savedAccount.getId().getValue(),
                savedAccount.getProfile().getFullName(),
                savedAccount.getEmail(),
                savedAccount.getStatus().name(),
                savedAccount.getProvider().name()
            ),
            result
        );
    }

    @Test
    void rejectsExistingEmailBeforeEncodingOrSaving() {
        CreateUserByEmailCommand command = validCommand();
        when(accountRepository.existsByEmail(command.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> service.executeForEmail(command));

        verify(accountRepository).existsByEmail(command.email());
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void doesNotSaveAccountWhenPasswordEncoderThrowsException() {
        CreateUserByEmailCommand command = validCommand();
        IllegalArgumentException failure = new IllegalArgumentException("Password cannot be encoded");

        when(accountRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenThrow(failure);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
            service.executeForEmail(command)
        );

        assertSame(failure, thrown);
        verify(accountRepository).existsByEmail(command.email());
        verify(passwordEncoder).encode(command.password());
        verify(accountRepository, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(
        strings = { "   ", "plain-text-password", "$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZabcdea" }
    )
    void doesNotSaveAccountWhenEncoderReturnsInvalidHash(String invalidEncodedPassword) {
        CreateUserByEmailCommand command = validCommand();
        when(accountRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenReturn(invalidEncodedPassword);

        assertThrows(IllegalArgumentException.class, () -> service.executeForEmail(command));

        verify(accountRepository).existsByEmail(command.email());
        verify(passwordEncoder).encode(command.password());
        verify(accountRepository, never()).save(any());
    }

    private CreateUserByEmailCommand validCommand() {
        return new CreateUserByEmailCommand("Nguyen Van A", "account@example.com", "TestPassword1!");
    }
}
