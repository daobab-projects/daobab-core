package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterByteArrayBased<T> implements DatabaseTypeConverter<byte[], T> {


    public byte[] readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }


}
