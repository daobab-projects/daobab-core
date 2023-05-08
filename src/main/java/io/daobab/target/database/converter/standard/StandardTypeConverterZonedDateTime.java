package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterZonedDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class StandardTypeConverterZonedDateTime implements TypeConverterZonedDateTimeBased<ZonedDateTime> {

    private final DataBaseTarget target;

    public StandardTypeConverterZonedDateTime(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public ZonedDateTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public ZonedDateTime convertReadingTarget(ZonedDateTime from) {
        return from;
    }

    @Override
    public String convertWritingTarget(ZonedDateTime to) {
        return target.getDatabaseDateConverter().toDatabaseZonedDateTime(to);
    }
}
