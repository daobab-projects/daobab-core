package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.Currency;

/**
 * JSON converter for {@link java.util.Currency}: written as its quoted ISO 4217 code (e.g. {@code "EUR"}) and
 * parsed with {@link java.util.Currency#getInstance(String)}. A natural fit for monetary columns that keep the
 * currency alongside the amount.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonCurrencyConverter extends JsonConverter<Currency> {

    @Override
    public void toJson(StringBuilder sb, Currency obj) {
        sb.append(QUOTE).append(obj.getCurrencyCode()).append(QUOTE);
    }

    @Override
    public Currency fromJson(String json) {
        return Currency.getInstance(json);
    }
}
