package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.math.BigDecimal;

/**
 * JSON converter for {@link java.math.BigDecimal}: written as its plain (non-scientific)
 * decimal text - a bare JSON number - and parsed back with {@code new BigDecimal(text)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
