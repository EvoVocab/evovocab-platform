package com.evovocab.user.infrastructure.adapter.out;

import com.evovocab.user.application.port.in.CreateUserAccountUseCase;
import com.evovocab.user.application.port.in.CreateUserByEmailCommand;
import com.evovocab.user.application.port.out.CreateUserByEmailData;
import com.evovocab.user.application.port.out.UserFacadePort;
import com.evovocab.user.application.port.out.UserResult;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeAdapter implements UserFacadePort {

    private CreateUserAccountUseCase createUserAccountUseCase;

    @Override
    public UserResult createUserAccountByEmail(CreateUserByEmailData data) {
        String fullName = data.fullName();
        String accountEmail = data.email();
        String accountPassword = data.password();
        CreateUserByEmailCommand command = new CreateUserByEmailCommand(fullName, accountEmail, accountPassword);
        return createUserAccountUseCase.executeForEmail(command);
    }
}
