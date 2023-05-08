package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBigIntegerBased;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterBigInteger implements TypeConverterBigIntegerBased<BigInteger> {

    @Override
    public BigInteger readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public BigInteger convertReadingTarget(BigInteger from) {
        return from;
    }

    @Override
    public String convertWritingTarget(BigInteger to) {
        return to == null ? null : to.toString();
    }
}
