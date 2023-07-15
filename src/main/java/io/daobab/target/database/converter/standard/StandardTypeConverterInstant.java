package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class StandardTypeConverterInstant extends TypeConverterLocalDateTimeBased<Instant> {

    private final DataBaseTarget target;

    public StandardTypeConverterInstant(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public Instant readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex).toInstant(ZoneOffset.UTC);
    }

    @Override
    public Instant convertReadingTarget(LocalDateTime from) {
        return from.toInstant(ZoneOffset.UTC);
    }

    @Override
    public String convertWritingTarget(Instant to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDateTime(LocalDateTime.ofInstant(to, ZoneOffset.UTC));
    }
}
