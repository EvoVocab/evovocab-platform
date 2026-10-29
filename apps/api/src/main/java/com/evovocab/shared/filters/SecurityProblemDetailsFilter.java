package com.evovocab.shared.filters;

import com.evovocab.shared.dto.responses.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityProblemDetailsFilter {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
        AuthenticationException ex,
        HttpServletRequest request
    ) {
        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title("Unauthorized")
            .status(HttpStatus.UNAUTHORIZED.value())
            .detail(ex.getMessage())
            .instance(request.getRequestURI())
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
        AuthorizationDeniedException ex,
        HttpServletRequest request
    ) {
        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title("Forbidden")
            .status(HttpStatus.FORBIDDEN.value())
            .detail("Bạn không có quyền truy cập tài nguyên này.")
            .instance(request.getRequestURI())
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException ex, HttpServletRequest request) {
        ErrorResponseDto response = ErrorResponseDto.builder()
            .type("about:blank")
            .title("Invalid Token")
            .status(HttpStatus.UNAUTHORIZED.value())
            .detail(ex.getMessage())
            .instance(request.getRequestURI())
            .timestamp(Instant.now().toEpochMilli())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(response);
    }
}
