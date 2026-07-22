package io.daobab.target.database.converter.type;


import io.daobab.converter.TypeConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A converter between a raw database (JDBC) value of type {@code F} and a Daobab column value of type {@code T}.
 * <p>
 * It extends {@link TypeConverter} with the reading side of the database round trip: {@link #readFromResultSet}
 * pulls the raw JDBC value out of a {@link ResultSet} and {@link #readAndConvert} additionally maps it to the
 * column type. The {@code type} package holds the abstract per-JDBC-type readers (an {@code Integer} reader, a
 * {@code String} reader, ...); the {@code standard} package holds the concrete converters registered per Java
 * type in the {@code DatabaseConverterManager}.
 *
 * @param <F> the raw database (JDBC) type
 * @param <T> the Daobab column type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface DatabaseTypeConverter<F, T> extends TypeConverter<F, T> {


    /**
     * Reads the raw value at the given column and converts it to the column type.
     *
     * @param rs          the result set positioned on the current row
     * @param columnIndex the 1-based column index
     * @return the converted column value
     * @throws SQLException if the value cannot be read
     */
    default T readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return convertReadingTarget(readFromResultSet(rs, columnIndex));
    }


    /**
     * Reads the raw database value at the given column of the current row.
     *
     * @param rs          the result set positioned on the current row
     * @param columnIndex the 1-based column index
     * @return the raw database value, or {@code null} when the column is SQL NULL
     * @throws SQLException if the value cannot be read
     */
    F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    /**
     * Whether {@link #convertWritingParameter} has to be applied before the value is bound to a
     * PreparedStatement (true when the driver cannot bind the column type directly). {@code false} by default.
     */
    default boolean needParameterConversion() {
        return false;
    }

    /**
     * Whether this converter resolves a foreign key into a whole entity (a {@code PrimaryKey} lookup).
     * {@code false} by default.
     */
    default boolean isEntityConverter() {
        return false;
    }

    /**
     * Whether this converter resolves a key into a list of entities. {@code false} by default.
     */
    default boolean isEntityListConverter() {
        return false;
    }

}
