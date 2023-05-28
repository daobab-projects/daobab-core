package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimestampBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StandardTypeConverterTimestamp extends TypeConverterTimestampBased<Timestamp> {

    private final DataBaseTarget target;

    public StandardTypeConverterTimestamp(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public Timestamp readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Timestamp convertReadingTarget(Timestamp from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Timestamp to) {
        return target.getDatabaseDateConverter().toDatabaseTimestamp(to);
    }
}
