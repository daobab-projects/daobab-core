package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Duration;

/**
 * JSON converter for {@link java.time.Duration}: written as its quoted ISO-8601 text
 * (e.g. {@code "PT1H30M"}) and parsed with {@link java.time.Duration#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonDurationConverter extends JsonConverter<Duration> {

    @Override
    public void toJson(StringBuilder sb, Duration obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public Duration fromJson(String json) {
        return Duration.parse(json);
    }
}
