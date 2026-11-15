package com.evovocab.shared.config;

import com.evovocab.shared.dto.responses.ErrorResponseDto;
import com.evovocab.shared.properties.AppProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final BuildProperties buildProperties;
    private final AppProperties appProperties;
    private static final Locale LOCALE = Locale.forLanguageTag("vi");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Bean
    public OpenAPI customOpenAPI() {
        final String appVersion = buildProperties.getVersion();
        final String appDescription = buildProperties.get("description");
        final String appName = appProperties.getName();
        final String appNameDefault = "EvoVocab API";
        final String appVersionDefault = "1.0.0";
        final String appDescriptionDefault = "Tài liệu API cho EvoVocab";

        final String systemName = "EvoVocab";
        final String systemDescription = appDescription != null ? appDescription : appDescriptionDefault;
        final String systemEmail = "evovocab2410@gmail.com";
        final String systemGithubUrl = "https://github.com/EvoVocab/evovocab-platform.git";
        final String systemLicenseName = "Apache 2.0";
        final String systemLicenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

        return new OpenAPI().info(
            new Info()
                .title(appName == null || appName.isEmpty() ? appNameDefault : appName)
                .version(appVersion == null || appVersion.isEmpty() ? appVersionDefault : appVersion)
                .description("Tai liệu API cho " + systemName + ". " + systemDescription)
                .contact(new Contact().name(systemName).email(systemEmail).url(systemGithubUrl))
                .license(new License().name(systemLicenseName).url(systemLicenseUrl))
        );
    }

    @Bean
    public OperationCustomizer validationErrorCustomizer() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        MessageInterpolator interpolator = factory.getMessageInterpolator();

        return (operation, handlerMethod) -> {
            for (MethodParameter param : handlerMethod.getMethodParameters()) {
                if (!param.hasParameterAnnotation(RequestBody.class)) {
                    continue;
                }

                Map<String, String> errors = collectErrors(validator, interpolator, param.getParameterType());
                if (errors.isEmpty()) {
                    continue;
                }

                ErrorResponseDto example = ErrorResponseDto.builder()
                    .type("about:blank")
                    .title("Unprocessable Entity")
                    .status(422)
                    .detail("Dữ liệu không hợp lệ.")
                    .instance("/api/v1/...")
                    .errors(errors)
                    .timestamp(1763188800000L)
                    .build();

                MediaType mediaType = new MediaType()
                    .schema(new Schema<>().$ref("#/components/schemas/ErrorResponseDto"))
                    .example(MAPPER.valueToTree(example));

                operation
                    .getResponses()
                    .addApiResponse(
                        "422",
                        new ApiResponse()
                            .description("Dữ liệu không hợp lệ")
                            .content(new Content().addMediaType("application/problem+json", mediaType))
                    );
                break;
            }
            return operation;
        };
    }

    private Map<String, String> collectErrors(Validator validator, MessageInterpolator interpolator, Class<?> type) {
        Map<String, String> errors = new LinkedHashMap<>();
        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(type);

        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            PropertyDescriptor prop = beanDescriptor.getConstraintsForProperty(field.getName());
            if (prop == null) {
                continue;
            }

            prop.getConstraintDescriptors()
                .stream()
                .findFirst()
                .ifPresent((d) -> errors.put(field.getName(), interpolate(interpolator, d)));
        }
        return errors;
    }

    private String interpolate(MessageInterpolator interpolator, ConstraintDescriptor<?> descriptor) {
        return interpolator.interpolate(
            descriptor.getMessageTemplate(),
            new MessageInterpolator.Context() {
                @Override
                public ConstraintDescriptor<?> getConstraintDescriptor() {
                    return descriptor;
                }

                @Override
                public Object getValidatedValue() {
                    return null;
                }

                @Override
                public <T> T unwrap(Class<T> type) {
                    throw new jakarta.validation.ValidationException();
                }
            },
            LOCALE
        );
    }
}
