package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Base converter for columns read from the database as a {@link ZonedDateTime}. The underlying
 * {@link java.sql.Timestamp} carries no zone, so the instant is interpreted at {@link ZoneOffset#UTC}.
 * Subclasses map that value to a concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code ZonedDateTime} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterZonedDateTimeBased<T> implements DatabaseTypeConverter<ZonedDateTime, T> {

    /**
     * Reads the column as a {@link java.sql.Timestamp} and returns it as a {@link ZonedDateTime} at UTC,
     * or {@code null} when the column is SQL NULL.
     */
    public ZonedDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp timestamp = rs.getTimestamp(columnIndex);
        if (timestamp == null) return null;

        return ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp.getTime()), ZoneOffset.UTC);
    }

}
