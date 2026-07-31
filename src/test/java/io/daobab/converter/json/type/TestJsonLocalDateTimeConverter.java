package io.daobab.converter.json.type;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Round-trips the {@link JsonLocalDateTimeConverter} at its millisecond precision: {@code toJson} then
 * {@code fromJson} must return the original value (truncated to milliseconds, the format's resolution).
 */
class TestJsonLocalDateTimeConverter {

    @Test
    void roundTripsAtMillisecondPrecision() {
        JsonLocalDateTimeConverter converter = new JsonLocalDateTimeConverter();
        LocalDateTime value = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        //the value is wrapped in quotes on write; fromJson receives the quoted form here and parses it
        String json = sb.toString();
        assertEquals(value, converter.fromJson(json.substring(1, json.length() - 1)));
    }
}
