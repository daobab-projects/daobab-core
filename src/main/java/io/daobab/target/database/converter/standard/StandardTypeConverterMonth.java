package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.Month;

/**
 * Standard converter for {@link Month} columns stored as their number 1-12: the database {@code Integer} is
 * mapped to a {@code Month} on reading and back to its number on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterMonth extends TypeConverterIntegerBased<Month> {

    /**
     * Maps the stored number (1-12) to a {@link Month}, or {@code null}.
     */
    @Override
    public Month convertReadingTarget(Integer from) {
        return from == null ? null : Month.of(from);
    }

    /** Renders the month as its number, or an empty string when {@code null}. */
    @Override
    public String convertWritingTarget(Month to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    /** Binds the month as its number (1-12), or {@code null}. */
    @Override
    public Object convertWritingParameter(Month to) {
        return to == null ? null : to.getValue();
    }
}
