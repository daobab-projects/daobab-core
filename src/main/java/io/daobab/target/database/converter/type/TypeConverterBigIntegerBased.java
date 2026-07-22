package io.daobab.target.database.converter.type;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code BigInteger} (through a {@code BigDecimal}).
 * Subclasses map that value to a concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code BigInteger} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterBigIntegerBased<T> implements DatabaseTypeConverter<BigInteger, T> {

    /**
     * Reads the column as a {@code BigDecimal} and narrows it to a {@code BigInteger} (fractional part dropped).
     */
    public BigInteger readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex).toBigInteger();
    }

}
