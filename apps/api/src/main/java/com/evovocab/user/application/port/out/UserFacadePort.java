package com.evovocab.user.application.port.out;

public interface UserFacadePort {
    UserResult createUserAccountByEmail(CreateUserByEmailData data);
}
