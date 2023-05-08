package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public interface TypeConverterZonedDateTimeBased<T> extends DatabaseTypeConverter<ZonedDateTime, T> {

    default ZonedDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp timestamp = rs.getTimestamp(columnIndex);
        if (timestamp == null) return null;

        return ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp.getTime()), ZoneOffset.UTC);
    }

}
