package com.evovocab.auth.application.port.in;

public interface RegisterUseCase {
    String executeForEmail(RegisterByEmailCommand command);
}
