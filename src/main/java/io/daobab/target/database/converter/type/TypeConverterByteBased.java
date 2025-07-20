package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterByteBased<T> implements DatabaseTypeConverter<Byte, T> {


    public Byte readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getByte(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
