package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterOffsetDateTimeBased;

import java.time.OffsetDateTime;

/**
 * Standard converter for {@link OffsetDateTime} columns ({@code TIMESTAMP WITH TIME ZONE}): read straight from
 * the database preserving the offset and bound directly (JDBC 4.2 drivers accept an {@code OffsetDateTime}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterOffsetDateTime extends TypeConverterOffsetDateTimeBased<OffsetDateTime> {

    /**
     * Returns the database value unchanged.
     */
    @Override
    public OffsetDateTime convertReadingTarget(OffsetDateTime from) {
        return from;
    }

    /** Renders the value as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(OffsetDateTime to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link OffsetDateTime} directly (JDBC 4.2 {@code TIMESTAMP WITH TIME ZONE}). */
    @Override
    public Object convertWritingParameter(OffsetDateTime to) {
        //JDBC 4.2 drivers bind an OffsetDateTime directly for TIMESTAMP WITH TIME ZONE
        return to;
    }
}
