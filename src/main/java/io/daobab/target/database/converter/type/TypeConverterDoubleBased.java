package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterDoubleBased<T> implements DatabaseTypeConverter<Double, T> {


    public Double readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }


}
