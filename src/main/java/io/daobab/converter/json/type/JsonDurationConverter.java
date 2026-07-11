package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Duration;

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
