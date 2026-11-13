package com.evovocab.user.domain.repository;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import java.util.Optional;

public interface UserAccountRepository {
    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(UserAccountId id);

    Optional<UserAccount> findByEmail(String email);
}
