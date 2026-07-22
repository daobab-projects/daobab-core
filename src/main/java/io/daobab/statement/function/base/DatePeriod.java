package io.daobab.statement.function.base;

/**
 * A date/time part, used by the interval and date functions (e.g. {@code EXTRACT}, {@code DATEADD},
 * {@code DATEDIFF}). Covers both single fields ({@code YEAR}, {@code MONTH}, ...) and the compound
 * ranges some engines accept ({@code DAY_HOUR}, {@code MINUTE_SECOND}, ...).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum DatePeriod {

    YEAR, QUARTER, MONTH, WEEK, DAY, HOUR, MINUTE, SECOND, MILLISECOND,
    TIMEZONE_HOUR, TIMEZONE_MINUTE, TIMEZONE_SECOND, YEAR_MONTH,
    DAY_HOUR, DAY_MINUTE, DAY_SECOND, DAY_MICROSECOND,
    HOUR_MINUTE, HOUR_SECOND,
    MINUTE_SECOND, HOUR_MICROSECOND, MINUTE_MICROSECOND, SECOND_MICROSECOND


}
