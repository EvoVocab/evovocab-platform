package com.evovocab.shared.config;

import com.evovocab.shared.jackson.NfcStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public SimpleModule unicodeNormalizationModule() {
        SimpleModule module = new SimpleModule("unicode-normalization");

        module.addDeserializer(String.class, new NfcStringDeserializer());
        return module;
    }
}
