package com.evovocab.user.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

class PasswordTest {

    private static final String VALID_HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode("TestPassword1!");

    @Test
    void rejectsPasswordHashWhenBcrypt2bCost10() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            Password.hashForPersistence(VALID_HASH)
        );
        assertEquals("Password must not be already hashed", exception.getMessage());
    }

    @Test
    void rejectsPasswordHashWhenNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            Password.hashForPersistence(null)
        );
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void acceptsPasswordWhenPlaintext() {
        assertEquals("TestPassword1!", Password.hashForPersistence("TestPassword1!").getHashedValue());
    }

    @Test
    void acceptsPasswordHashWhenTruncated() {
        String value = VALID_HASH.substring(0, 59);
        assertEquals(value, Password.hashForPersistence(value).getHashedValue());
    }

    @Test
    void acceptsPasswordHashWhenBcryptVersionDoesNotMatchPattern() {
        String value = VALID_HASH.replace("$2b$", "$2a$");
        assertEquals(value, Password.hashForPersistence(value).getHashedValue());
    }

    @Test
    void acceptsPasswordHashWhenBcryptCostDoesNotMatchPattern() {
        String value = VALID_HASH.replace("$10$", "$12$");
        assertEquals(value, Password.hashForPersistence(value).getHashedValue());
    }

    @Test
    void rejectsPasswordWhenEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            Password.hashForPersistence("")
        );
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void preservesWhitespaceWhenValueIsNotEmpty() {
        String value = "   ";
        assertEquals(value, Password.hashForPersistence(value).getHashedValue());
    }
}
