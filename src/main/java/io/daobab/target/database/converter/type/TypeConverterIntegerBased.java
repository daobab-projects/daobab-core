package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterIntegerBased<T> implements DatabaseTypeConverter<Integer, T> {


    public Integer readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }


}
