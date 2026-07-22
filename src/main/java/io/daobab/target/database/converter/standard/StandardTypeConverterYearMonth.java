package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.YearMonth;

/**
 * Standard converter for {@link YearMonth} columns stored as strings (e.g. {@code 2026-07}): parsed on reading,
 * rendered back to its text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterYearMonth extends TypeConverterStringBased<YearMonth> {

    /**
     * Parses the stored string into a {@link YearMonth}, or {@code null}.
     */
    @Override
    public YearMonth convertReadingTarget(String from) {
        return from == null ? null : YearMonth.parse(from);
    }

    /** Renders the {@link YearMonth} as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(YearMonth to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link YearMonth} as its string form, or {@code null}. */
    @Override
    public Object convertWritingParameter(YearMonth to) {
        return to == null ? null : to.toString();
    }
}
