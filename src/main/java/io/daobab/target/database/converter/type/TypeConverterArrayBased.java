package io.daobab.target.database.converter.type;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterArrayBased<F> extends DatabaseTypeConverter<F, Array> {


    default Array readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }


}
