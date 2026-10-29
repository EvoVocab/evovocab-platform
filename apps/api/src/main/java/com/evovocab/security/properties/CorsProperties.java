package com.evovocab.security.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "security.cors")
public class CorsProperties {

    @NotEmpty(message = "Allowed origins (CORS) cannot be empty")
    private List<String> allowedOrigins;

    @NotEmpty(message = "Allowed methods (CORS) cannot be empty")
    private List<String> allowedMethods;

    @NotEmpty(message = "Allowed headers (CORS) cannot be empty")
    private List<String> allowedHeaders;

    @NotNull
    private Boolean allowCredentials;
}
