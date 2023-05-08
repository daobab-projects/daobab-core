package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBigDecimalBased;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterBigDecimal implements TypeConverterBigDecimalBased<BigDecimal> {

    @Override
    public BigDecimal readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public BigDecimal convertReadingTarget(BigDecimal from) {
        return from;
    }

    @Override
    public String convertWritingTarget(BigDecimal to) {
        return to == null ? null : to.toString();
    }
}
