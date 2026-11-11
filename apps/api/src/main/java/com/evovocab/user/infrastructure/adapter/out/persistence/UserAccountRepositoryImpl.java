package com.evovocab.user.infrastructure.adapter.out.persistence;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.repository.UserAccountRepository;
import com.evovocab.user.infrastructure.mapper.UserAccountMapper;
import java.util.Objects;
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
        Objects.requireNonNull(account.getProfile(), "UserAccount profile is required");
        UserAccountEntity accEntity = jpaRepository.findById(account.getId()).orElse(null);
        if (accEntity == null) {
            accEntity = mapper.toJpaEntity(account);
        } else {
            mapper.updateJpaEntity(account, accEntity);
            UserProfileEntity profileEntity = accEntity.getProfile();
            if (profileEntity == null) {
                accEntity.setProfile(mapper.toJpaEntity(account.getProfile()));
            } else {
                mapper.updateJpaEntity(account.getProfile(), profileEntity);
            }
        }
        UserAccountEntity accSavedEntity = jpaRepository.save(accEntity);
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
