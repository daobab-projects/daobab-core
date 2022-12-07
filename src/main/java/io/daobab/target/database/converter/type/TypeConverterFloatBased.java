package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterFloatBased<F> extends DatabaseTypeConverter<F, Float> {


    default Float readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }


}
