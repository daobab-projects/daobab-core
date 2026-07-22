package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Base converter for columns read from the database as a {@link LocalDateTime} (a {@link java.sql.Timestamp}
 * read in the default time zone and converted to it). Subclasses map that value to a concrete Daobab column
 * type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code LocalDateTime} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterLocalDateTimeBased<T> implements DatabaseTypeConverter<LocalDateTime, T> {

    /**
     * The calendar (default time zone) the timestamp is read with.
     */
    private final Calendar calendarZone = Calendar.getInstance(TimeZone.getDefault());

    /**
     * Reads the column as a {@link java.sql.Timestamp} in the default time zone and converts it to a
     * {@link LocalDateTime}, or {@code null} when the column is SQL NULL.
     */
    public LocalDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnIndex, calendarZone);
        return date == null ? null : date.toLocalDateTime();
    }


}
