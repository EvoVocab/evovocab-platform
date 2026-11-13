package com.evovocab.user.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserAccountRepository extends JpaRepository<UserAccountEntity, UUID> {
    @Override
    Optional<UserAccountEntity> findById(UUID id);

    Optional<UserAccountEntity> findByEmail(String email);
}
