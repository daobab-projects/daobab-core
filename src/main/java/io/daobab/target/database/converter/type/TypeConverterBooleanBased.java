package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterBooleanBased<F> extends DatabaseTypeConverter<F, Boolean> {


    default Boolean readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }


}
