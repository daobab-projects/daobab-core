package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterLongBased<F> extends DatabaseTypeConverter<F, Long> {


    default Long readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }


}
