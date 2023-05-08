package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public interface TypeConverterLocalDateTimeBased<T> extends DatabaseTypeConverter<LocalDateTime, T> {

    default LocalDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnIndex);
        return date == null ? null : date.toLocalDateTime();
    }


}
