package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterByteBased<T> extends DatabaseTypeConverter<Byte, T> {


    default Byte readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }


}
