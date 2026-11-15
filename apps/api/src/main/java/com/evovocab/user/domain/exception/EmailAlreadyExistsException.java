package com.evovocab.user.domain.exception;

import com.evovocab.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AppException {

    public EmailAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Conflict", "Email đã tồn tại");
    }
}
