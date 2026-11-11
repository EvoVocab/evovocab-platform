package com.evovocab.user.infrastructure.mapper;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserAccountEntity;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountEntity toJpaEntity(UserAccount account);

    UserAccount toDomainEntity(UserAccountEntity entity);

    @Mapping(target = "account", ignore = true)
    UserProfileEntity toJpaEntity(UserProfile profile);

    UserProfile toDomainEntity(UserProfileEntity entity);
}
