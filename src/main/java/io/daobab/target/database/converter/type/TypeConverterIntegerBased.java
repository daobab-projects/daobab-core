package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterIntegerBased<T> extends DatabaseTypeConverter<Integer, T> {


    default Integer readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }


}
