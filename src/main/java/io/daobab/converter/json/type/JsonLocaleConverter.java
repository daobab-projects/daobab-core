package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.Locale;

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
