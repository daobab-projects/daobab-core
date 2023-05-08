package io.daobab.target.database.converter.type;


import io.daobab.converter.TypeConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseTypeConverter<F, T> extends TypeConverter<F, T> {


    default T readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return convertReadingTarget(readFromResultSet(rs, columnIndex));
    }


    F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    default boolean needParameterConversion() {
        return false;
    }

    default boolean isEntityConverter() {
        return false;
    }

    default boolean isEntityListConverter() {
        return false;
    }

}
