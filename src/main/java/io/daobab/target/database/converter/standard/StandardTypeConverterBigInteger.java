package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBigIntegerBased;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code BigInteger} columns: read straight from the database, written as their decimal
 * text, and bound as a {@link java.math.BigDecimal} PreparedStatement parameter (drivers accept no BigInteger).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterBigInteger extends TypeConverterBigIntegerBased<BigInteger> {

    /**
     * Reads the {@code BigInteger} straight from the result set (no conversion needed).
     */
    @Override
    public BigInteger readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public BigInteger convertReadingTarget(BigInteger from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(BigInteger to) {
        return to == null ? null : to.toString();
    }

    /** Widens the value to a {@link java.math.BigDecimal} for binding, or {@code null}. */
    @Override
    public Object convertWritingParameter(BigInteger to) {
        return to == null ? null : new java.math.BigDecimal(to);
    }
}
