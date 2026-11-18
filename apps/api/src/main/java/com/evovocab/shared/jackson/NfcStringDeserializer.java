package com.evovocab.shared.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.io.IOException;
import java.text.Normalizer;

public class NfcStringDeserializer extends StringDeserializer implements ContextualDeserializer {

    // Nếu field được đánh dấu với annotation @SkipNfcNormalization thì StringDeserializer(Deserializer mặc định cho class String)
    // sẽ mặc định thay vì NfcStringDeserializer
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property != null && property.getAnnotation(SkipNfcNormalization.class) != null) {
            return StringDeserializer.instance;
        }
        return this;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = super.deserialize(p, ctxt);
        return value == null ? null : Normalizer.normalize(value, Normalizer.Form.NFC);
    }
}
