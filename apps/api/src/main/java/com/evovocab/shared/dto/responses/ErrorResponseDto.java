package com.evovocab.shared.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    @Builder.Default
    private String type = "about:blank";

    private String title;
    private int status;
    private String detail;
    private String instance;
    private Map<String, String> errors;
    private long timestamp;
}
