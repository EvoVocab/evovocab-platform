package com.evovocab.user.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserAccountRepository extends JpaRepository<UserAccountEntity, UUID> {
    @Override
    @EntityGraph(attributePaths = "profile")
    Optional<UserAccountEntity> findById(UUID id);

    @EntityGraph(attributePaths = "profile")
    Optional<UserAccountEntity> findByEmail(String email);
}
