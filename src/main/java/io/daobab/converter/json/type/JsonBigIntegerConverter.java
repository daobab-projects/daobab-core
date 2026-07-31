package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.math.BigInteger;

/**
 * JSON converter for {@link java.math.BigInteger}: written as its decimal text - a bare JSON
 * number - and parsed back with {@code new BigInteger(text)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonBigIntegerConverter extends JsonConverter<BigInteger> {
    @Override
    public void toJson(StringBuilder sb, BigInteger obj) {
        sb.append(obj.toString());
    }

    @Override
    public BigInteger fromJson(String json) {
        return new BigInteger(json);
    }
}
