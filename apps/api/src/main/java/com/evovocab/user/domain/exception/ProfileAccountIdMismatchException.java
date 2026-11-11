package com.evovocab.user.domain.exception;

import com.evovocab.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class ProfileAccountIdMismatchException extends AppException {

    public ProfileAccountIdMismatchException() {
        super(HttpStatus.BAD_REQUEST, "Profile Account ID Mismatch", "Profile accountId must match account id");
    }
}
