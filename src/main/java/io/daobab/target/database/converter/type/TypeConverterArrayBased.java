package io.daobab.target.database.converter.type;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterArrayBased<T> extends DatabaseTypeConverter<Array, T> {


    default Array readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }


}
