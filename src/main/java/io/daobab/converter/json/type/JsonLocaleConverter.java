package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.Locale;

/**
 * JSON converter for {@link java.util.Locale}: written as its quoted BCP 47 language tag
 * (e.g. {@code "pl-PL"}) and parsed with {@link java.util.Locale#forLanguageTag(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocaleConverter extends JsonConverter<Locale> {

    @Override
    public void toJson(StringBuilder sb, Locale obj) {
        sb.append(QUOTE).append(obj.toLanguageTag()).append(QUOTE);
    }

    @Override
    public Locale fromJson(String json) {
        return Locale.forLanguageTag(json);
    }
}
