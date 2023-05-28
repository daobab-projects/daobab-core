package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public abstract class TypeConverterTimeBased<T> implements DatabaseTypeConverter<Time, T> {


    public Time readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }


}
