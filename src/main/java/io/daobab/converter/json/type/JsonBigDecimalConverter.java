package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.math.BigDecimal;

public class JsonBigDecimalConverter extends JsonConverter<BigDecimal> {
    @Override
    public void toJson(StringBuilder sb, BigDecimal obj) {
        sb.append(obj.toPlainString());
    }

    @Override
    public BigDecimal fromJson(String json) {
        return new BigDecimal(json);
    }
}
