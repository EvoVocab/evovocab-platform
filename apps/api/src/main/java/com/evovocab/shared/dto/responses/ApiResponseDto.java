package com.evovocab.shared.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private int status;
    private String message;
    private T data;

    @Builder.Default
    private long timestamp = Instant.now().toEpochMilli();

    public static <T> ApiResponseDto<T> success(T data) {
        return ApiResponseDto.<T>builder().status(HttpStatus.OK.value()).message("Success").data(data).build();
    }

    public static <T> ApiResponseDto<T> success(T data, String message) {
        return ApiResponseDto.<T>builder().status(HttpStatus.OK.value()).message(message).data(data).build();
    }

    public static <T> ApiResponseDto<T> created(T data) {
        return ApiResponseDto.<T>builder()
            .status(HttpStatus.CREATED.value())
            .message("Created successfully")
            .data(data)
            .build();
    }
}
