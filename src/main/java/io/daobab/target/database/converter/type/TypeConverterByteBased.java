package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterByteBased<F> extends DatabaseTypeConverter<F, Byte> {


    default Byte readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }


}
