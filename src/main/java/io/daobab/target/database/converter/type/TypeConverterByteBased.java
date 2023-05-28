package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterByteBased<T> implements DatabaseTypeConverter<Byte, T> {


    public Byte readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }


}
