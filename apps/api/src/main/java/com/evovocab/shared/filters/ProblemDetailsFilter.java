package com.evovocab.shared.filters;

import com.evovocab.shared.dto.responses.ErrorResponseDto;
import com.evovocab.shared.exceptions.AppException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProblemDetailsFilter {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title("Unprocessable Entity")
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .detail("Validation failed")
            .instance(request.getRequestURI())
            .errors(validationErrors)
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleAppException(AppException ex, HttpServletRequest request) {
        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title(ex.getTitle())
            .status(ex.getStatus().value())
            .detail(ex.getMessage())
            .instance(request.getRequestURI())
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(ex.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllOtherExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception: ", ex);

        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title("Internal Server Error")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .detail(ex.getMessage())
            .instance(request.getRequestURI())
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(response);
    }
}
