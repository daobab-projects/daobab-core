package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.Base64;

/**
 * JSON converter for a {@code byte[]}: written as a quoted Base64 string (RFC 4648) and decoded back on read.
 * Base64 is used because its alphabet ({@code A-Za-z0-9+/=}) contains no characters that need JSON escaping, so
 * the output is always valid JSON and round-trips through the entity/plate reader unchanged.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonByteArrayConverter extends JsonConverter<byte[]> {

    @Override
    public void toJson(StringBuilder sb, byte[] obj) {
        sb.append(QUOTE).append(Base64.getEncoder().encodeToString(obj)).append(QUOTE);
    }

    @Override
    public byte[] fromJson(String json) {
        return Base64.getDecoder().decode(json);
    }
}
