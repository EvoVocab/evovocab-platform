package com.evovocab.user.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

class PasswordTest {

    private static final String VALID_HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode("TestPassword1!");
    private static final String EMPTY_ERROR_MSG = "Password cannot be null or empty";
    private static final String INVALID_HASH_ERROR_MSG = "Password must be already hashed with suitable Bcrypt";

    @Test
    void acceptsPasswordHashWhenValidBcrypt2bCost10() {
        Password password = Password.fromHash(VALID_HASH);
        assertEquals(VALID_HASH, password.getHashedValue());
    }

    @Test
    void returnsEmptyStringWhenGetEmptyPassword() {
        Password password = Password.getEmptyPassword();
        assertEquals("", password.getHashedValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsPasswordHashWhenNullOrEmpty(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            Password.fromHash(input)
        );
        assertEquals(EMPTY_ERROR_MSG, exception.getMessage());
    }

    @ParameterizedTest(name = "Rejects invalid hash: {0}")
    @MethodSource("provideInvalidHashes")
    void rejectsPasswordHashWhenFormatIsNotSuitableBcrypt(String caseDescription, String invalidInput) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            Password.fromHash(invalidInput)
        );
        assertEquals(INVALID_HASH_ERROR_MSG, exception.getMessage());
    }

    private static Stream<Arguments> provideInvalidHashes() {
        return Stream.of(
            Arguments.of("Blank whitespace", "   "),
            Arguments.of("Plaintext password", "TestPassword1!"),
            Arguments.of("Truncated hash (59 chars)", VALID_HASH.substring(0, 59)),
            Arguments.of("Unsupported BCrypt version ($2a$)", VALID_HASH.replace("$2b$", "$2a$")),
            Arguments.of("Unsupported BCrypt cost ($12$)", VALID_HASH.replace("$10$", "$12$"))
        );
    }
}
