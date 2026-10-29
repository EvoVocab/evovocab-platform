package com.evovocab.shared.config;

import com.evovocab.shared.properties.AppProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final BuildProperties buildProperties;
    private final AppProperties appProperties;

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
}
