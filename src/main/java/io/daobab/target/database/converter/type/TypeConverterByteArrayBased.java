package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterByteArrayBased<T> extends DatabaseTypeConverter<byte[], T> {


    default byte[] readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }


}
