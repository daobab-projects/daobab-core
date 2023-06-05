package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.math.BigInteger;

public class JsonBigIntegerConverter implements JsonConverter<BigInteger> {
    @Override
    public String toJson(BigInteger obj) {
        return obj.toString();
    }

    @Override
    public BigInteger fromJson(String json) {
        return new BigInteger(json);
    }
}
