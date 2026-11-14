package com.evovocab.user.infrastructure.mapper;

import com.evovocab.user.domain.model.Password;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserAccountEntity;
import com.evovocab.user.infrastructure.adapter.out.persistence.UserProfileEntity;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    @Mapping(target = "lifecycleMetadata.createdAt", source = "createdAt")
    @Mapping(target = "lifecycleMetadata.updatedAt", source = "updatedAt")
    @Mapping(target = "lifecycleMetadata.deletedAt", source = "deletedAt")
    UserAccountEntity toJpaEntity(UserAccount account);

    @Mapping(target = "createdAt", source = "lifecycleMetadata.createdAt")
    @Mapping(target = "updatedAt", source = "lifecycleMetadata.updatedAt")
    @Mapping(target = "deletedAt", source = "lifecycleMetadata.deletedAt")
    UserAccount toDomainEntity(UserAccountEntity entity);

    default String toJpaPassword(Password password) {
        return password == null ? null : password.getHashedValue();
    }

    default Password toDomainPassword(String password) {
        return password == null ? null : Password.fromHash(password);
    }

    default UUID toJpaId(UserAccountId id) {
        return id == null ? null : id.getValue();
    }

    default UserAccountId toDomainId(UUID id) {
        return id == null ? null : UserAccountId.parse(id.toString());
    }

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auditMetadata.createdAt", source = "createdAt")
    @Mapping(target = "auditMetadata.updatedAt", source = "updatedAt")
    UserProfileEntity toJpaEntity(UserProfile profile);

    @Mapping(target = "createdAt", source = "auditMetadata.createdAt")
    @Mapping(target = "updatedAt", source = "auditMetadata.updatedAt")
    @Mapping(target = "accountId", source = "id")
    UserProfile toDomainEntity(UserProfileEntity entity);
}
