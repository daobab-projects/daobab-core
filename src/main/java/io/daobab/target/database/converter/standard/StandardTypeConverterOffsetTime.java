package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterOffsetTimeBased;

import java.time.OffsetTime;

/**
 * Standard converter for {@link OffsetTime} columns ({@code TIME WITH TIME ZONE}): read straight from the
 * database preserving the offset and bound directly (JDBC 4.2 drivers accept an {@code OffsetTime}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterOffsetTime extends TypeConverterOffsetTimeBased<OffsetTime> {

    /**
     * Returns the database value unchanged.
     */
    @Override
    public OffsetTime convertReadingTarget(OffsetTime from) {
        return from;
    }

    /** Renders the value as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(OffsetTime to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link OffsetTime} directly (JDBC 4.2 {@code TIME WITH TIME ZONE}). */
    @Override
    public Object convertWritingParameter(OffsetTime to) {
        //JDBC 4.2 drivers bind an OffsetTime directly for TIME WITH TIME ZONE
        return to;
    }
}
