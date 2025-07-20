package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterBooleanBased<T> implements DatabaseTypeConverter<Boolean, T> {


    public Boolean readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getBoolean(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
