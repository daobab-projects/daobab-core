package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Standard converter for {@link LocalDateTime} columns: read straight from the database (through a
 * {@code java.sql.Timestamp}) and written through the target's dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterLocalDateTime extends TypeConverterLocalDateTimeBased<LocalDateTime> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterLocalDateTime(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link LocalDateTime} straight from the result set.
     */
    @Override
    public LocalDateTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public LocalDateTime convertReadingTarget(LocalDateTime from) {
        return from;
    }

    /** Renders the date-time using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(LocalDateTime to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDateTime(to);
    }
}
