package com.evovocab.user.application.port.in;

import com.evovocab.user.application.port.out.UserResult;

public interface CreateUserAccountUseCase {
    UserResult execute(CreateUserAccountCommand command);
}
