package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public interface TypeConverterLocalDateBased<T> extends DatabaseTypeConverter<LocalDate, T> {

    default LocalDate readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Date date = rs.getDate(columnIndex);
        return date == null ? null : date.toLocalDate();
    }


}
