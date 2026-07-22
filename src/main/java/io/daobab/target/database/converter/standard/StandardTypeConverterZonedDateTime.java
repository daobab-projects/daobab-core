package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterZonedDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Standard converter for {@link ZonedDateTime} columns: read straight from the database, written through the
 * target's dialect date converter and bound as an {@code OffsetDateTime} parameter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterZonedDateTime extends TypeConverterZonedDateTimeBased<ZonedDateTime> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterZonedDateTime(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link ZonedDateTime} straight from the result set.
     */
    @Override
    public ZonedDateTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public ZonedDateTime convertReadingTarget(ZonedDateTime from) {
        return from;
    }

    /** Renders the value using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(ZonedDateTime to) {
        return target.getDatabaseDateConverter().toDatabaseZonedDateTime(to);
    }

    /** Binds the value as an {@code OffsetDateTime}, or {@code null}. */
    @Override
    public Object convertWritingParameter(ZonedDateTime to) {
        return to == null ? null : to.toOffsetDateTime();
    }
}
