package com.evovocab.user.application.port.in;

import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;

public record CreateUserAccountCommand(
    String fullName,
    String email,
    String password,
    UserAccountStatus status,
    UserAccountProvider provider
) {}
