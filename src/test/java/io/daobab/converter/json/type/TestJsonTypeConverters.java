package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.converter.json.JsonConverterManager;
import io.daobab.error.DaobabException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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

    private static <T> String toJsonString(JsonConverter<T> converter, T value) {
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        return sb.toString();
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
    void voidIsAlwaysNull() {
        JsonVoidConverter converter = new JsonVoidConverter();
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, null);
        assertEquals("null", sb.toString());
        assertNull(converter.fromJson("null"));
    }

    @Test
    void sqlXmlSerializesTheContentAndRejectsReconstruction() {
        //java.sql.SQLXML is a driver interface: a proxy returns the XML text from getString()
        SQLXML xml = (SQLXML) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{SQLXML.class},
                (proxy, method, args) -> "getString".equals(method.getName()) ? "<a>x\"y</a>" : null);

        JsonSqlXmlConverter converter = new JsonSqlXmlConverter();
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, xml);
        //the content is quoted and escaped like any JSON string (the inner quote becomes \")
        assertEquals("\"<a>x\\\"y</a>\"", sb.toString());

        assertThrows(DaobabException.class, () -> converter.fromJson("<a>x</a>"));
    }

    @Test
    void arraySerializesElementsAndRejectsReconstruction() {
        //java.sql.Array is a driver interface: a proxy returns the backing array from getArray()
        java.sql.Array array = (java.sql.Array) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{java.sql.Array.class},
                (proxy, method, args) -> "getArray".equals(method.getName()) ? new Integer[]{1, 2, 3} : null);

        JsonArrayConverter converter = new JsonArrayConverter();
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, array);
        //each element is serialized by its own registered converter (Integer -> bare number)
        assertEquals("[1,2,3]", sb.toString());

        assertThrows(DaobabException.class, () -> converter.fromJson("[1,2,3]"));
    }

    @Test
    void arraySerializesStringElementsQuoted() {
        java.sql.Array array = (java.sql.Array) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{java.sql.Array.class},
                (proxy, method, args) -> "getArray".equals(method.getName()) ? new String[]{"a", "b"} : null);

        JsonArrayConverter converter = new JsonArrayConverter();
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, array);
        assertEquals("[\"a\",\"b\"]", sb.toString());
    }

    // ---------------------------------------------------------------- numeric (bare JSON numbers)

    @Test
    void bigDecimalPlainText() {
        assertRoundTrip(new JsonBigDecimalConverter(), new BigDecimal("12.50"), "12.50");
    }

    @Test
    void bigInteger() {
        assertRoundTrip(new JsonBigIntegerConverter(), new BigInteger("123456789012345678901234567890"),
                "123456789012345678901234567890");
    }

    @Test
    void booleanValue() {
        assertRoundTrip(new JsonBooleanConverter(), Boolean.TRUE, "true");
        assertRoundTrip(new JsonBooleanConverter(), Boolean.FALSE, "false");
    }

    @Test
    void byteValue() {
        assertRoundTrip(new JsonByteConverter(), (byte) 7, "7");
    }

    @Test
    void shortValue() {
        assertRoundTrip(new JsonShortConverter(), (short) 300, "300");
    }

    @Test
    void integerValue() {
        assertRoundTrip(new JsonIntegerConverter(), 42, "42");
    }

    @Test
    void longValue() {
        assertRoundTrip(new JsonLongConverter(), 9_999_999_999L, "9999999999");
    }

    @Test
    void doubleTrimsTrailingZeros() {
        assertRoundTrip(new JsonDoubleConverter(), 2.5, "2.5");
        assertRoundTrip(new JsonDoubleConverter(), 2.0, "2");
    }

    @Test
    void floatTrimsTrailingZeros() {
        assertRoundTrip(new JsonFloatConverter(), 1.25f, "1.25");
        assertRoundTrip(new JsonFloatConverter(), 3.0f, "3");
    }

    @Test
    void doubleNonFiniteBecomesNull() {
        JsonDoubleConverter converter = new JsonDoubleConverter();
        assertEquals("null", toJsonString(converter, Double.NaN));
        assertEquals("null", toJsonString(converter, Double.POSITIVE_INFINITY));
        assertEquals("null", toJsonString(converter, Double.NEGATIVE_INFINITY));
    }

    @Test
    void floatNonFiniteBecomesNull() {
        JsonFloatConverter converter = new JsonFloatConverter();
        assertEquals("null", toJsonString(converter, Float.NaN));
        assertEquals("null", toJsonString(converter, Float.POSITIVE_INFINITY));
    }

    // ---------------------------------------------------------------- string-like (quoted)

    @Test
    void stringQuotesAndEscapes() {
        assertRoundTrip(new JsonStringConverter(), "a\"b\n", "\"a\\\"b\\n\"");
    }

    @Test
    void byteArrayIsQuotedBase64ValidJson() {
        JsonByteArrayConverter converter = new JsonByteArrayConverter();
        //arbitrary binary content, including bytes that used to produce raw control chars / a bare backslash
        byte[] value = {0, 1, 2, (byte) 200, '"', '\\'};
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        //quoted Base64 - valid JSON, no characters that need escaping
        assertEquals("\"" + java.util.Base64.getEncoder().encodeToString(value) + "\"", sb.toString());
        assertArrayEquals(value, converter.fromJson(unquote(sb.toString())));
    }

    // ---------------------------------------------------------------- date / time

    @Test
    void localDate() {
        assertRoundTrip(new JsonLocalDateConverter(), LocalDate.of(2026, 7, 11), "\"2026-07-11\"");
    }

    @Test
    void localTimePadsMilliseconds() {
        assertRoundTrip(new JsonLocalTimeConverter(), LocalTime.of(8, 30, 15, 7_000_000), "\"08:30:15.007\"");
    }

    @Test
    void localDateTime() {
        assertRoundTrip(new JsonLocalDateTimeConverter(),
                LocalDateTime.of(2026, 7, 11, 8, 5, 9, 123_000_000), "\"2026-07-11T08:05:09.123\"");
    }

    @Test
    void zonedDateTime() {
        ZonedDateTime value = ZonedDateTime.of(2026, 7, 11, 13, 5, 9, 123_000_000, ZoneOffset.ofHours(2));
        assertRoundTrip(new JsonZonedDateTimeConverter(), value, "\"2026-07-11T13:05:09.123+02:00\"");
    }

    @Test
    void yearsBelow1000AreZeroPaddedToFourDigits() {
        //ISO parsers need a 4-digit year, so a year < 1000 must be zero-padded to round-trip (was broken)
        assertRoundTrip(new JsonLocalDateConverter(), LocalDate.of(753, 4, 21), "\"0753-04-21\"");
        assertRoundTrip(new JsonLocalDateTimeConverter(),
                LocalDateTime.of(753, 4, 21, 1, 2, 3, 0), "\"0753-04-21T01:02:03.000\"");
        assertRoundTrip(new JsonYearMonthConverter(), YearMonth.of(753, 4), "\"0753-04\"");
        assertRoundTrip(new JsonZonedDateTimeConverter(),
                ZonedDateTime.of(99, 1, 2, 3, 4, 5, 0, ZoneOffset.UTC), "\"0099-01-02T03:04:05.000Z\"");
    }

    @Test
    void instantAsUtcDateTime() {
        Instant value = LocalDateTime.of(2026, 7, 11, 13, 5, 9, 123_000_000).toInstant(ZoneOffset.UTC);
        assertRoundTrip(new JsonInstantConverter(), value, "\"2026-07-11T13:05:09.123\"");
    }

    @Test
    void sqlDate() {
        assertRoundTrip(new JsonSqlDateConverter(), java.sql.Date.valueOf("2026-07-11"), "\"2026-07-11\"");
    }

    @Test
    void sqlTime() {
        assertRoundTrip(new JsonSqlTimeConverter(), Time.valueOf("08:30:15"), "\"08:30:15.000\"");
    }

    @Test
    void timestamp() {
        assertRoundTrip(new JsonTimestampConverter(), Timestamp.valueOf("2026-07-11 13:05:09.123"),
                "\"2026-07-11T13:05:09.123\"");
    }

    @Test
    void utilDateRoundTripsAtMillisecondPrecision() {
        JsonDateConverter converter = new JsonDateConverter();
        //a whole-second instant avoids sub-millisecond loss across the system-zone conversion
        java.util.Date value = new java.util.Date(1_752_000_000_000L);
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, value);
        assertEquals(value, converter.fromJson(unquote(sb.toString())));
    }

    // ---------------------------------------------------------------- java.time numeric-coded

    @Test
    void year() {
        assertRoundTrip(new JsonLocalYearConverter(), Year.of(2026), "\"2026\"");
    }

    @Test
    void monthAsNumber() {
        assertRoundTrip(new JsonLocalMonthConverter(), Month.JULY, "\"7\"");
    }

    @Test
    void dayOfWeekAsNumber() {
        assertRoundTrip(new JsonLocalDayOfWeekConverter(), DayOfWeek.FRIDAY, "\"5\"");
    }

    // ---------------------------------------------------------------- additional value types

    @Test
    void zoneId() {
        assertRoundTrip(new JsonZoneIdConverter(), ZoneId.of("Europe/Warsaw"), "\"Europe/Warsaw\"");
    }

    @Test
    void currency() {
        assertRoundTrip(new JsonCurrencyConverter(), Currency.getInstance("EUR"), "\"EUR\"");
    }

    // ---------------------------------------------------------------- enum / optional / collections / entity

    @Test
    void enumByName() {
        assertRoundTrip(new JsonEnumConverter<>(Color.class), Color.RED, "\"RED\"");
    }

    @Test
    void optionalPresentAndEmpty() {
        JsonOptionalConverter converter = new JsonOptionalConverter(new JsonIntegerConverter());
        StringBuilder present = new StringBuilder();
        converter.toJson(present, Optional.of(5));
        assertEquals("5", present.toString());
        assertEquals(Optional.of(5), converter.fromJson("5"));

        StringBuilder empty = new StringBuilder();
        converter.toJson(empty, Optional.empty());
        assertEquals("null", empty.toString());
        assertEquals(Optional.empty(), converter.fromJson("null"));
    }

    @Test
    void listOfNumbersRoundTrips() {
        JsonListConverter converter = new JsonListConverter(new JsonIntegerConverter());
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, List.of(1, 2, 3));
        assertEquals("[1,2,3]", sb.toString());
        //multi-element scalar collections now round-trip (used to throw NumberFormatException on "1,2,3")
        assertEquals(List.of(1, 2, 3), converter.fromJson("[1,2,3]"));
        assertEquals(List.of(), converter.fromJson("[]"));
    }

    @Test
    void listOfStringsRoundTripsAndStripsElementQuotes() {
        JsonListConverter converter = new JsonListConverter(new JsonStringConverter());
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, List.of("a", "b,c"));
        //the comma inside the second quoted element must not split it
        assertEquals("[\"a\",\"b,c\"]", sb.toString());
        assertEquals(List.of("a", "b,c"), converter.fromJson(sb.toString()));
    }

    @Test
    void setSerializesAsJsonArray() {
        JsonSetConverter converter = new JsonSetConverter(new JsonIntegerConverter());
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, new java.util.LinkedHashSet<>(List.of(9)));
        assertEquals("[9]", sb.toString());
        assertEquals(new java.util.HashSet<>(List.of(4, 5, 6)), converter.fromJson("[4,5,6]"));
    }

    @Test
    void collectionSerializesAsJsonArray() {
        JsonCollectionConverter converter = new JsonCollectionConverter(new JsonIntegerConverter());
        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, List.of(4, 5));
        assertEquals("[4,5]", sb.toString());
    }

    @Test
    void daobabEntityDelegatesToEntityJson() {
        io.daobab.test.dao.table.Actor actor = new io.daobab.test.dao.table.Actor()
                .setActorId(7).setFirstName("NICK").setLastName("WAHLBERG");

        JsonDaobabEntityConverter<io.daobab.test.dao.table.Actor> converter =
                new JsonDaobabEntityConverter<>(io.daobab.test.dao.table.Actor.class);

        StringBuilder sb = new StringBuilder();
        converter.toJson(sb, actor);
        //the entity delegates to its own toJson (the field values appear in the payload)
        assertEquals(actor.toJson(), sb.toString());

        io.daobab.test.dao.table.Actor back = converter.fromJson(sb.toString());
        assertEquals(7, back.getActorId());
        assertEquals("NICK", back.getFirstName());
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
        //the converters added for parity with the DB type converters
        assertConverterType(java.sql.Array.class, JsonArrayConverter.class);
        assertConverterType(SQLXML.class, JsonSqlXmlConverter.class);
        assertConverterType(Void.class, JsonVoidConverter.class);
        //the additional commonly-used value types
        assertConverterType(ZoneId.class, JsonZoneIdConverter.class);
        assertConverterType(Currency.class, JsonCurrencyConverter.class);
    }

    @Test
    void stringEscapesEveryControlCharacter() {
        //a raw character below 0x20 is not valid inside a JSON string, so the ones without a short escape
        //have to come out as \\uXXXX
        assertEquals("\"a\\u0000b\\u001fc\"", toJsonString(new JsonStringConverter(), "a\u0000b\u001fc"));
        assertEquals("a\u0000b\u001fc", new JsonStringConverter().fromJson("a\\u0000b\\u001fc"));
    }

    @Test
    void stringRejectsANonHexUnicodeEscape() {
        assertThrows(DaobabException.class, () -> new JsonStringConverter().fromJson("\\u00zz"));
    }

    @Test
    void characterEscapesAControlCharacter() {
        assertEquals("\"\\u0001\"", toJsonString(new JsonCharacterConverter(), '\u0001'));
        assertEquals(Character.valueOf('\u0001'), new JsonCharacterConverter().fromJson("\\u0001"));
    }

    private enum Color {RED, GREEN}
}
