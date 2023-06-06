package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterFloatBased<T> implements DatabaseTypeConverter<Float, T> {


    public Float readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }


}
