package com.evovocab.user.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

class PasswordTest {

    private static final String VALID_HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode("TestPassword1!");

    @Test
    void acceptsPasswordHashWhenBcrypt2bCost10() {
        Password password = Password.hashForPersistence(VALID_HASH);

        assertEquals(VALID_HASH, password.getHashedValue());
        assertTrue(password.isHashed());
    }

    @Test
    void rejectsPasswordHashWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> Password.hashForPersistence(null));
    }

    @Test
    void rejectsPasswordHashWhenPlaintext() {
        assertThrows(IllegalArgumentException.class, () -> Password.hashForPersistence("TestPassword1!"));
    }

    @Test
    void rejectsPasswordHashWhenTruncated() {
        assertThrows(IllegalArgumentException.class, () -> Password.hashForPersistence(VALID_HASH.substring(0, 59)));
    }

    @Test
    void rejectsPasswordHashWhenBcryptVersionUnsupported() {
        assertThrows(IllegalArgumentException.class, () ->
            Password.hashForPersistence(VALID_HASH.replaceFirst("\\$2b\\$", "\\$2a\\$"))
        );
    }

    @Test
    void rejectsPasswordHashWhenBcryptCostUnsupported() {
        assertThrows(IllegalArgumentException.class, () ->
            Password.hashForPersistence(VALID_HASH.replaceFirst("\\$10\\$", "\\$12\\$"))
        );
    }
}
