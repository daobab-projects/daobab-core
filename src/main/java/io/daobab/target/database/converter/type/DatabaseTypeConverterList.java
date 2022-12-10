package io.daobab.target.database.converter.type;


import io.daobab.converter.TypeConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseTypeConverterList<F, T> extends TypeConverter<F, List<T>> {


    default List<T> readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return convertReadingTarget(readFromResultSet(rs, columnIndex));
    }


    F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    default boolean needParameterConversion() {
        return false;
    }

    default boolean isEntityConverter() {
        return false;
    }

}
