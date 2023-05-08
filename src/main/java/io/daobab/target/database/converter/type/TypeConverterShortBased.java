package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterShortBased<T> extends DatabaseTypeConverter<Short, T> {


    default Short readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }


}
