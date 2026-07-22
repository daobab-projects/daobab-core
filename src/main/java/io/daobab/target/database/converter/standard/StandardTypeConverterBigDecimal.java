package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBigDecimalBased;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code BigDecimal} columns: read straight from the database, written as their
 * plain decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterBigDecimal extends TypeConverterBigDecimalBased<BigDecimal> {

    /**
     * Reads the {@code BigDecimal} straight from the result set (no conversion needed).
     */
    @Override
    public BigDecimal readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public BigDecimal convertReadingTarget(BigDecimal from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(BigDecimal to) {
        return to == null ? null : to.toString();
    }
}
