package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.Duration;

/**
 * Standard converter for {@link Duration} columns stored as ISO-8601 strings: parsed on reading, rendered back
 * to its ISO text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterDuration extends TypeConverterStringBased<Duration> {

    /**
     * Parses the stored ISO-8601 string into a {@link Duration}, or {@code null}.
     */
    @Override
    public Duration convertReadingTarget(String from) {
        return from == null ? null : Duration.parse(from);
    }

    /** Renders the {@link Duration} as a quoted ISO-8601 SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(Duration to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link Duration} as its ISO-8601 string, or {@code null}. */
    @Override
    public Object convertWritingParameter(Duration to) {
        return to == null ? null : to.toString();
    }
}
