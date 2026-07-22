package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimestampBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Standard converter for {@link Timestamp} columns: read straight from the database and written through the
 * target's dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterTimestamp extends TypeConverterTimestampBased<Timestamp> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterTimestamp(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link Timestamp} straight from the result set.
     */
    @Override
    public Timestamp readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Timestamp convertReadingTarget(Timestamp from) {
        return from;
    }

    /** Renders the timestamp using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(Timestamp to) {
        return target.getDatabaseDateConverter().toDatabaseTimestamp(to);
    }
}
