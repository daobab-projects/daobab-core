package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterShortBased<T> implements DatabaseTypeConverter<Short, T> {


    public Short readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getShort(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
