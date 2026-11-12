package com.evovocab.user.domain.model;

import java.util.regex.Pattern;

public class Password {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile(
        "\\$2[aby]\\$(?:0[4-9]|[12][0-9]|3[01])\\$[./A-Za-z0-9]{22}[./A-Za-z0-9]{31}"
    );

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public boolean isHashed() {
        return value != null && BCRYPT_PATTERN.matcher(value).matches();
    }

    public static Password hashForPersistence(String value) {
        if (value == null || !BCRYPT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Password must have a valid format of bcrypt");
        }
        return new Password(value);
    }

    public String getHashedValue() {
        if (!isHashed()) {
            throw new IllegalStateException("Cannot persist a raw password");
        }
        return value;
    }
}
