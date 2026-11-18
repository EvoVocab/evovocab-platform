package com.evovocab.auth.infrastructure.adapter.in.web.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.Normalizer;

public class EmailDeserializer extends StdDeserializer<String> {

    public EmailDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        return Normalizer.normalize(trimmed, Normalizer.Form.NFC);
    }
}
