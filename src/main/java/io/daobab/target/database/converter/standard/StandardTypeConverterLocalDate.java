package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class StandardTypeConverterLocalDate implements TypeConverterLocalDateBased<LocalDate> {

    private final DataBaseTarget target;

    public StandardTypeConverterLocalDate(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public LocalDate readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public LocalDate convertReadingTarget(LocalDate from) {
        return from;
    }

    @Override
    public String convertWritingTarget(LocalDate to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDate(to);
    }
}
