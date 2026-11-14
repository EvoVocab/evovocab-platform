package com.evovocab.auth.application.service;

import com.evovocab.auth.application.port.in.RegisterByEmailCommand;
import com.evovocab.auth.application.port.in.RegisterUseCase;
import com.evovocab.user.application.port.out.CreateUserByEmailData;
import com.evovocab.user.application.port.out.UserFacadePort;
import com.evovocab.user.application.port.out.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterByEmailService implements RegisterUseCase {

    private final UserFacadePort userFacadePort;

    @Override
    public String executeForEmail(RegisterByEmailCommand command) {
        CreateUserByEmailData data = new CreateUserByEmailData(command.fullName(), command.email(), command.password());
        UserResult userResult = userFacadePort.createUserAccountByEmail(data);
        return userResult.email();
    }
}
