package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterIntegerBased<F> extends DatabaseTypeConverter<F, Integer> {


    default Integer readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }


}
