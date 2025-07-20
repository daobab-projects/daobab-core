package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterDoubleBased<T> implements DatabaseTypeConverter<Double, T> {


    public Double readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getDouble(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
