package com.evovocab.user.infrastructure.mapper;

import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserAccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountEntity toJpaEntity(UserAccount account);

    UserAccount toDomainEntity(UserAccountEntity entity);
}
