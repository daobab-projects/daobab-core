package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class TypeConverterLocalDateTimeBased<T> implements DatabaseTypeConverter<LocalDateTime, T> {

    private final Calendar calendarZone = Calendar.getInstance(TimeZone.getDefault());

    public LocalDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnIndex, calendarZone);
        return date == null ? null : date.toLocalDateTime();
    }


}
