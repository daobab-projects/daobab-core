package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.converter.json.JsonConverterManager;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URL;
import java.time.*;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Round-trip coverage for the JSON type converters: {@code toJson} produces the expected text and
 * {@code fromJson} rebuilds the original value from the unquoted payload (the converters wrap the value
 * in quotes on write and receive the already unquoted string on read).
 */
class TestJsonTypeConverters {

    private static <T> void assertRoundTrip(JsonConverter<T> converter, T value, String expectedJson) {
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        assertEquals(expectedJson, sb.toString(), "toJson output");
        assertEquals(value, converter.fromJson(unquote(sb.toString())), "fromJson round trip");
    }

    private static String unquote(String s) {
        if (s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static void assertConverterType(Class<?> type, Class<? extends JsonConverter<?>> expected) {
        JsonConverter<?> converter = JsonConverterManager.INSTANCE.getTypeConverter(type)
                .orElseThrow(() -> new AssertionError("no converter registered for " + type));
        assertEquals(expected, converter.getClass(), "converter for " + type);
    }

    @Test
    void uuid() {
        UUID value = UUID.fromString("3f2504e0-4f89-41d3-9a0c-0305e82c3301");
        assertRoundTrip(new JsonUuidConverter(), value, "\"3f2504e0-4f89-41d3-9a0c-0305e82c3301\"");
    }

    @Test
    void locale() {
        assertRoundTrip(new JsonLocaleConverter(), Locale.forLanguageTag("pl-PL"), "\"pl-PL\"");
    }

    @Test
    void duration() {
        assertRoundTrip(new JsonDurationConverter(), Duration.ofMinutes(90), "\"PT1H30M\"");
    }

    @Test
    void period() {
        assertRoundTrip(new JsonPeriodConverter(), Period.of(1, 2, 3), "\"P1Y2M3D\"");
    }

    @Test
    void uri() {
        URI value = URI.create("https://www.daobab.io/docs?q=1");
        assertRoundTrip(new JsonUriConverter(), value, "\"https://www.daobab.io/docs?q=1\"");
    }

    @Test
    void url() throws Exception {
        //URL.equals may hit the network, so compare the textual form instead of the object
        JsonUrlConverter converter = new JsonUrlConverter();
        URL value = URI.create("https://www.daobab.io/docs").toURL();
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        assertEquals("\"https://www.daobab.io/docs\"", sb.toString());
        assertEquals("https://www.daobab.io/docs", converter.fromJson(unquote(sb.toString())).toString());
    }

    @Test
    void yearMonthPadsSingleDigitMonth() {
        assertRoundTrip(new JsonYearMonthConverter(), YearMonth.of(2026, 3), "\"2026-03\"");
        assertRoundTrip(new JsonYearMonthConverter(), YearMonth.of(2026, 11), "\"2026-11\"");
    }

    @Test
    void monthDay() {
        assertRoundTrip(new JsonMonthDayConverter(), MonthDay.of(7, 11), "\"--07-11\"");
    }

    @Test
    void offsetDateTime() {
        OffsetDateTime value = OffsetDateTime.of(2026, 7, 11, 13, 5, 9, 123_000_000, ZoneOffset.ofHours(2));
        assertRoundTrip(new JsonOffsetDateTimeConverter(), value, "\"2026-07-11T13:05:09.123+02:00\"");
    }

    @Test
    void offsetTimePadsMilliseconds() {
        OffsetTime value = OffsetTime.of(8, 30, 15, 7_000_000, ZoneOffset.ofHours(-5));
        assertRoundTrip(new JsonOffsetTimeConverter(), value, "\"08:30:15.007-05:00\"");
    }

    @Test
    void characterPlain() {
        assertRoundTrip(new JsonCharacterConverter(), 'A', "\"A\"");
    }

    @Test
    void characterEscaped() {
        assertRoundTrip(new JsonCharacterConverter(), '\n', "\"\\n\"");
    }

    @Test
    void characterUnicodeAboveAscii() {
        //'ł' (U+0142) is emitted as a \\uXXXX escape and rebuilt from it
        assertRoundTrip(new JsonCharacterConverter(), 'ł', "\"\\u0142\"");
    }

    @Test
    void managerRegistersTheNewTypeConverters() {
        assertConverterType(UUID.class, JsonUuidConverter.class);
        assertConverterType(Locale.class, JsonLocaleConverter.class);
        assertConverterType(Character.class, JsonCharacterConverter.class);
        assertConverterType(char.class, JsonCharacterConverter.class);
        assertConverterType(OffsetDateTime.class, JsonOffsetDateTimeConverter.class);
        assertConverterType(OffsetTime.class, JsonOffsetTimeConverter.class);
        assertConverterType(YearMonth.class, JsonYearMonthConverter.class);
        assertConverterType(MonthDay.class, JsonMonthDayConverter.class);
        assertConverterType(Duration.class, JsonDurationConverter.class);
        assertConverterType(Period.class, JsonPeriodConverter.class);
        assertConverterType(URI.class, JsonUriConverter.class);
        assertConverterType(URL.class, JsonUrlConverter.class);
    }
}
