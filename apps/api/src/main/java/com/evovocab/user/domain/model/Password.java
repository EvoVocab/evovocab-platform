package com.evovocab.user.domain.model;

import com.evovocab.shared.utils.BcryptUtils;

public class Password {

    private final String value;

    public static Password getEmptyPassword() {
        return new Password("");
    }

    private Password(String value) {
        this.value = value;
    }

    public static Password hashForPersistence(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (BcryptUtils.checkIfPasswordIsHashed(value)) {
            throw new IllegalArgumentException("Password must not be already hashed");
        }

        return new Password(value);
    }

    public String getHashedValue() {
        return value;
    }
}
