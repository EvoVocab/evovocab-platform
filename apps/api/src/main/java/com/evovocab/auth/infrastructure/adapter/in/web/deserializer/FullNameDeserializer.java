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
        // Thay các spaces liên tiếp bằng 1 space duy nhất
        String normalizedSpaces = trimmed.replaceAll("\\s+", " ");

        String[] dividedFullName = normalizedSpaces.split(" ");

        // Locale.ROOT áp dụng quy tắc chuẩn của Unicode quốc tế, không bị chi phối bởi Locale riêng của bất kỳ quốc gia nào.
        // Các kí tự tiếng việt đều nằm trong Unicode, nên Java có thể xử lý mà không sai lệch với Locale.ROOT
        // Locale.getDefault() (mặc định) phụ thuộc vào Locale mặc định của server (ví dụ: ở Thổ Nhĩ Kỳ chữ 'i' viết hoa thành 'İ').
        String titleCaseFullName = Arrays.stream(dividedFullName)
            .map((word) -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT))
            .collect(Collectors.joining(" "));

        return Normalizer.normalize(titleCaseFullName, Normalizer.Form.NFC);
    }
}
