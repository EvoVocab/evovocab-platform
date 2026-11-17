package com.evovocab.auth.infrastructure.adapter.in.web;

import com.evovocab.auth.application.port.in.RegisterByEmailCommand;
import com.evovocab.auth.application.port.in.RegisterUseCase;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;

    @PostMapping("/v1/auth/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterByEmailCommand command = new RegisterByEmailCommand(
            request.fullName(),
            request.email(),
            request.password()
        );

        String registeredEmail = registerUseCase.executeForEmail(command);

        RegisterResponse response = new RegisterResponse(registeredEmail);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
