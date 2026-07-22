package io.daobab.target.database.converter.type;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code BigDecimal}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code BigDecimal} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterBigDecimalBased<T> implements DatabaseTypeConverter<BigDecimal, T> {

    /**
     * Reads the column as a {@code BigDecimal}, or {@code null} when the column is SQL NULL.
     */
    public BigDecimal readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

}
