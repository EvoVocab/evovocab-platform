package com.evovocab.user.infrastructure.adapter.out.persistence;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.repository.UserAccountRepository;
import com.evovocab.user.infrastructure.mapper.UserAccountMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final JpaUserAccountRepository jpaRepository;
    private final UserAccountMapper mapper;

    public UserAccountRepositoryImpl(JpaUserAccountRepository jpaRepository, UserAccountMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserAccount save(UserAccount account) {
        UserAccountEntity accSavedEntity = jpaRepository.save(mapper.toJpaEntity(account));
        return mapper.toDomainEntity(accSavedEntity);
    }

    @Override
    public Optional<UserAccount> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomainEntity);
    }
}
