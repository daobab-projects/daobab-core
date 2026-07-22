package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.Period;

/**
 * Standard converter for {@link Period} columns stored as ISO-8601 strings: parsed on reading, rendered back
 * to its ISO text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterPeriod extends TypeConverterStringBased<Period> {

    /**
     * Parses the stored ISO-8601 string into a {@link Period}, or {@code null}.
     */
    @Override
    public Period convertReadingTarget(String from) {
        return from == null ? null : Period.parse(from);
    }

    /** Renders the {@link Period} as a quoted ISO-8601 SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(Period to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link Period} as its ISO-8601 string, or {@code null}. */
    @Override
    public Object convertWritingParameter(Period to) {
        return to == null ? null : to.toString();
    }
}
