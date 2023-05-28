package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonBigDecimalConverter implements JsonConverter<BigDecimal> {
    @Override
    public String toJson(BigDecimal obj) {
        return obj.toPlainString();
    }

    @Override
    public BigDecimal fromJson(String json) {
        return new BigDecimal(json);
    }
}
