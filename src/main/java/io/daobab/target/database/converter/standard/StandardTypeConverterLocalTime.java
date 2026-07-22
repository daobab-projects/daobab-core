package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Standard converter for {@link LocalTime} columns: read as a {@code java.sql.Time} and converted to a
 * {@code LocalTime}, written through the target's dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterLocalTime extends TypeConverterTimeBased<LocalTime> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterLocalTime(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads a {@code java.sql.Time} and converts it to a {@link LocalTime}.
     */
    @Override
    public LocalTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Time sqlTime = readFromResultSet(rs, columnIndex);
        return sqlTime.toLocalTime();
    }

    /** Converts the read {@code java.sql.Time} to a {@link LocalTime}. */
    @Override
    public LocalTime convertReadingTarget(Time from) {
        return from.toLocalTime();
    }

    /** Renders the time using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(LocalTime to) {
        return target.getDatabaseDateConverter().toDatabaseLocalTime(to);
    }
}
