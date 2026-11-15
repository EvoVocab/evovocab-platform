package com.evovocab.user.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserAccountIdTest {

    @Test
    void parsesUserAccountIdWhenUuidValid() {
        UUID uuid = UUID.randomUUID();
        UserAccountId first = UserAccountId.parse(uuid.toString());

        assertEquals(uuid, first.getValue());
        assertEquals(uuid.toString(), first.toString());
    }

    @Test
    void comparesUserAccountIdsWhenUuidValuesMatch() {
        UUID uuid = UUID.randomUUID();
        UserAccountId first = UserAccountId.parse(uuid.toString());
        UserAccountId same = UserAccountId.parse(uuid.toString());

        assertEquals(first, first);
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
    }

    @Test
    void distinguishesUserAccountIdWhenOtherValueDiffers() {
        UserAccountId first = UserAccountId.generate();
        UUID uuid = first.getValue();

        assertNotEquals(first, UserAccountId.generate());
        assertNotEquals(first, null);
        assertNotEquals(first, uuid);
    }

    @Test
    void rejectsUserAccountIdWhenUuidMalformed() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountId.parse("not-a-uuid"));
    }
}
