package com.evovocab.user.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class UserAccountId {

    private final UUID value;

    private UserAccountId(UUID value) {
        this.value = Objects.requireNonNull(value, "UserAccountId cannot be null");
    }

    public static UserAccountId generate() {
        return new UserAccountId(UUID.randomUUID());
    }

    public static UserAccountId parse(String value) {
        return new UserAccountId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof UserAccountId id && value.equals(id.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
