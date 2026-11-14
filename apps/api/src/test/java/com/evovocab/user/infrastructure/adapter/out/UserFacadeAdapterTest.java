package com.evovocab.user.infrastructure.adapter.out;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.evovocab.user.application.port.in.CreateUserAccountUseCase;
import com.evovocab.user.application.port.in.CreateUserByEmailCommand;
import com.evovocab.user.application.port.out.CreateUserByEmailData;
import com.evovocab.user.application.port.out.UserResult;
import com.evovocab.user.domain.exception.EmailAlreadyExistsException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserFacadeAdapterTest {

    @Mock
    private CreateUserAccountUseCase createUserAccountUseCase;

    @InjectMocks
    private UserFacadeAdapter adapter;

    @Test
    void mapsEmailRegistrationDataToCommandAndReturnsUseCaseResult() {
        CreateUserByEmailData data = new CreateUserByEmailData("Nguyen Van A", "account@example.com", "TestPassword1!");
        CreateUserByEmailCommand command = new CreateUserByEmailCommand(data.fullName(), data.email(), data.password());
        UserResult expected = new UserResult(UUID.randomUUID(), data.fullName(), data.email(), "INACTIVE", "EMAIL");
        when(createUserAccountUseCase.executeForEmail(command)).thenReturn(expected);

        assertSame(expected, adapter.createUserAccountByEmail(data));

        verify(createUserAccountUseCase).executeForEmail(command);
        verifyNoMoreInteractions(createUserAccountUseCase);
    }

    @Test
    void propagatesDuplicateEmailFailureFromUseCase() {
        CreateUserByEmailData data = new CreateUserByEmailData("Nguyen Van A", "account@example.com", "TestPassword1!");
        CreateUserByEmailCommand command = new CreateUserByEmailCommand(data.fullName(), data.email(), data.password());
        EmailAlreadyExistsException failure = new EmailAlreadyExistsException();
        when(createUserAccountUseCase.executeForEmail(command)).thenThrow(failure);

        assertSame(
            failure,
            assertThrows(EmailAlreadyExistsException.class, () -> adapter.createUserAccountByEmail(data))
        );

        verify(createUserAccountUseCase).executeForEmail(command);
        verifyNoMoreInteractions(createUserAccountUseCase);
    }
}
