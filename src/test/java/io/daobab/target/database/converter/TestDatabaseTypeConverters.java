package io.daobab.target.database.converter;

import io.daobab.target.database.converter.standard.*;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.*;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Conversion coverage for the database type converters: reading a raw JDBC value into the target type,
 * rendering the inline SQL literal ({@code convertWritingTarget}) and the bound parameter
 * ({@code convertWritingParameter}), including the null handling. The {@code readFromResultSet} step is a
 * thin wrapper over {@code ResultSet.getXxx} and is not exercised here.
 */
class TestDatabaseTypeConverters {

    @Test
    void uri() {
        StandardTypeConverterURI c = new StandardTypeConverterURI();
        URI value = URI.create("https://www.daobab.io/docs?q=1");

        assertEquals(value, c.convertReadingTarget("https://www.daobab.io/docs?q=1"));
        assertEquals("'https://www.daobab.io/docs?q=1'", c.convertWritingTarget(value));
        assertEquals("https://www.daobab.io/docs?q=1", c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
        assertNull(c.convertWritingParameter(null));
    }

    @Test
    void locale() {
        StandardTypeConverterLocale c = new StandardTypeConverterLocale();
        Locale value = Locale.forLanguageTag("pl-PL");

        assertEquals(value, c.convertReadingTarget("pl-PL"));
        assertEquals("'pl-PL'", c.convertWritingTarget(value));
        assertEquals("pl-PL", c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
    }

    @Test
    void character() {
        StandardTypeConverterCharacter c = new StandardTypeConverterCharacter();

        assertEquals('A', c.convertReadingTarget("A"));
        assertEquals("'A'", c.convertWritingTarget('A'));
        assertEquals("A", c.convertWritingParameter('A'));
        //an escaped apostrophe is doubled for the inline literal
        assertEquals("''''", c.convertWritingTarget('\''));
        assertNull(c.convertReadingTarget(null));
        assertNull(c.convertReadingTarget(""));
    }

    @Test
    void duration() {
        StandardTypeConverterDuration c = new StandardTypeConverterDuration();
        Duration value = Duration.ofMinutes(90);

        assertEquals(value, c.convertReadingTarget("PT1H30M"));
        assertEquals("'PT1H30M'", c.convertWritingTarget(value));
        assertEquals("PT1H30M", c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
    }

    @Test
    void period() {
        StandardTypeConverterPeriod c = new StandardTypeConverterPeriod();
        Period value = Period.of(1, 2, 3);

        assertEquals(value, c.convertReadingTarget("P1Y2M3D"));
        assertEquals("'P1Y2M3D'", c.convertWritingTarget(value));
        assertEquals("P1Y2M3D", c.convertWritingParameter(value));
    }

    @Test
    void yearMonth() {
        StandardTypeConverterYearMonth c = new StandardTypeConverterYearMonth();
        YearMonth value = YearMonth.of(2026, 7);

        assertEquals(value, c.convertReadingTarget("2026-07"));
        assertEquals("'2026-07'", c.convertWritingTarget(value));
        assertEquals("2026-07", c.convertWritingParameter(value));
    }

    @Test
    void monthDay() {
        StandardTypeConverterMonthDay c = new StandardTypeConverterMonthDay();
        MonthDay value = MonthDay.of(7, 11);

        assertEquals(value, c.convertReadingTarget("--07-11"));
        assertEquals("'--07-11'", c.convertWritingTarget(value));
        assertEquals("--07-11", c.convertWritingParameter(value));
    }

    @Test
    void year() {
        StandardTypeConverterYear c = new StandardTypeConverterYear();
        Year value = Year.of(2026);

        assertEquals(value, c.convertReadingTarget(2026));
        //numeric type -> unquoted inline literal, Integer bound parameter
        assertEquals("2026", c.convertWritingTarget(value));
        assertEquals(2026, c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
        assertNull(c.convertWritingParameter(null));
    }

    @Test
    void month() {
        StandardTypeConverterMonth c = new StandardTypeConverterMonth();

        assertEquals(Month.JULY, c.convertReadingTarget(7));
        assertEquals("7", c.convertWritingTarget(Month.JULY));
        assertEquals(7, c.convertWritingParameter(Month.JULY));
        assertNull(c.convertReadingTarget(null));
    }

    @Test
    void dayOfWeek() {
        StandardTypeConverterDayOfWeek c = new StandardTypeConverterDayOfWeek();

        assertEquals(DayOfWeek.MONDAY, c.convertReadingTarget(1));
        assertEquals("1", c.convertWritingTarget(DayOfWeek.MONDAY));
        assertEquals(1, c.convertWritingParameter(DayOfWeek.MONDAY));
        assertNull(c.convertReadingTarget(null));
    }

    @Test
    void offsetDateTime() {
        StandardTypeConverterOffsetDateTime c = new StandardTypeConverterOffsetDateTime();
        OffsetDateTime value = OffsetDateTime.of(2026, 7, 11, 13, 5, 9, 0, ZoneOffset.ofHours(2));

        assertEquals(value, c.convertReadingTarget(value));
        assertEquals("'2026-07-11T13:05:09+02:00'", c.convertWritingTarget(value));
        //bound directly for a TIMESTAMP WITH TIME ZONE column
        assertEquals(value, c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
    }

    @Test
    void offsetTime() {
        StandardTypeConverterOffsetTime c = new StandardTypeConverterOffsetTime();
        OffsetTime value = OffsetTime.of(8, 30, 15, 0, ZoneOffset.ofHours(-5));

        assertEquals(value, c.convertReadingTarget(value));
        assertEquals("'08:30:15-05:00'", c.convertWritingTarget(value));
        assertEquals(value, c.convertWritingParameter(value));
        assertNull(c.convertReadingTarget(null));
    }
}
