package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.DayOfWeek;

/**
 * Standard converter for {@link DayOfWeek} columns stored as their number 1 (Monday) - 7 (Sunday): the database
 * {@code Integer} is mapped to a {@code DayOfWeek} on reading and back to its number on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterDayOfWeek extends TypeConverterIntegerBased<DayOfWeek> {

    /**
     * Maps the stored number (1-7) to a {@link DayOfWeek}, or {@code null}.
     */
    @Override
    public DayOfWeek convertReadingTarget(Integer from) {
        return from == null ? null : DayOfWeek.of(from);
    }

    /** Renders the day as its number, or an empty string when {@code null}. */
    @Override
    public String convertWritingTarget(DayOfWeek to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    /** Binds the day as its number (1-7), or {@code null}. */
    @Override
    public Object convertWritingParameter(DayOfWeek to) {
        return to == null ? null : to.getValue();
    }
}
