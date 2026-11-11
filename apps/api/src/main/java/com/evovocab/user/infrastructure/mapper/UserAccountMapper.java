package com.evovocab.user.infrastructure.mapper;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserAccountEntity;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountEntity toJpaEntity(UserAccount account);

    UserAccount toDomainEntity(UserAccountEntity entity);

    @Mapping(target = "account", ignore = true)
    UserProfileEntity toJpaEntity(UserProfile profile);

    UserProfile toDomainEntity(UserProfileEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    void updateJpaEntity(UserAccount account, @MappingTarget UserAccountEntity entity);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    void updateJpaEntity(UserProfile profile, @MappingTarget UserProfileEntity entity);
}
