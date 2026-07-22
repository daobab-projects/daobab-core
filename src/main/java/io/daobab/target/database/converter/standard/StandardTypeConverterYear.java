package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.Year;

/**
 * Standard converter for {@link Year} columns stored as an integer: the database {@code Integer} is mapped to a
 * {@code Year} on reading and back to its numeric value on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterYear extends TypeConverterIntegerBased<Year> {

    /**
     * Maps the stored integer to a {@link Year}, or {@code null}.
     */
    @Override
    public Year convertReadingTarget(Integer from) {
        return from == null ? null : Year.of(from);
    }

    /** Renders the year as its numeric text, or an empty string when {@code null}. */
    @Override
    public String convertWritingTarget(Year to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    /** Binds the year as its {@code int} value, or {@code null}. */
    @Override
    public Object convertWritingParameter(Year to) {
        return to == null ? null : to.getValue();
    }
}
