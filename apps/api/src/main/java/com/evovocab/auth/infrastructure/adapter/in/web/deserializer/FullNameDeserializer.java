package com.evovocab.auth.infrastructure.adapter.in.web.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class FullNameDeserializer extends StdDeserializer<String> {

    public FullNameDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        String normalizedSpaces = trimmed.replaceAll("\\s+", " "); // thay the cac dau cach lien tiep bang mot space duy nhat

        String[] dividedFullName = normalizedSpaces.split(" ");

        // Voi Locale mac dinh (truyen ron) thi phụ thuộc vào cấu hình của hệ điều hành hoặc JVM nơi ứng dụng chạy, Locale.ROOT luôn cho kết quả đồng nhất trên mọi máy chủ.
        String titleCaseFullName = Arrays.stream(dividedFullName)
            .map((word) -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT))
            .collect(Collectors.joining(" "));

        return Normalizer.normalize(titleCaseFullName, Normalizer.Form.NFC);
    }
}
