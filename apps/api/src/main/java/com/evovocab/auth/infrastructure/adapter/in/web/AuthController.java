package com.evovocab.auth.infrastructure.adapter.in.web;

import com.evovocab.auth.application.port.in.RegisterByEmailCommand;
import com.evovocab.auth.application.port.in.RegisterUseCase;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterResponse;
import com.evovocab.shared.dto.responses.ApiResponseDto;
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
    public ResponseEntity<ApiResponseDto<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterByEmailCommand command = new RegisterByEmailCommand(
            request.fullName(),
            request.email(),
            request.password()
        );

        String registeredEmail = registerUseCase.executeForEmail(command);
        RegisterResponse result = new RegisterResponse(registeredEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.created(result, "Đăng ký thành công"));
    }
}
