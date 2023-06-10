package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.math.BigInteger;

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
