package com.evovocab.user.domain.repository;

import com.evovocab.user.domain.model.UserAccount;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository {
    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(UUID id);

    Optional<UserAccount> findByEmail(String email);
}
