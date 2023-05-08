package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class StandardTypeConverterLocalDateTime implements TypeConverterLocalDateTimeBased<LocalDateTime> {

    private final DataBaseTarget target;

    public StandardTypeConverterLocalDateTime(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public LocalDateTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public LocalDateTime convertReadingTarget(LocalDateTime from) {
        return from;
    }

    @Override
    public String convertWritingTarget(LocalDateTime to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDateTime(to);
    }
}
