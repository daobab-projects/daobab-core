package io.daobab.target.database.converter.type;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterArrayBased<T> implements DatabaseTypeConverter<Array, T> {


    public Array readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }


}
