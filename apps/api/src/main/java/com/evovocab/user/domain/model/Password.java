package com.evovocab.user.domain.model;

import java.util.regex.Pattern;

public class Password {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\$2b\\$10\\$[./A-Za-z0-9]{22}[./A-Za-z0-9]{31}");

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public boolean isHashed() {
        return this.value != null && BCRYPT_PATTERN.matcher(this.value).matches();
    }

    public static Password hashForPersistence(String value) {
        if (value == null || !BCRYPT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Password must have a valid format of bcrypt");
        }
        return new Password(value);
    }

    public String getHashedValue() {
        return value;
    }
}
