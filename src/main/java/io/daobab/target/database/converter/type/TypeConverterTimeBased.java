package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public interface TypeConverterTimeBased<T> extends DatabaseTypeConverter<Time, T> {


    default Time readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }


}
