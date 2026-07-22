package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * Standard converter for {@link Time} columns: read straight from the database and written through the target's
 * dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterTime extends TypeConverterTimeBased<Time> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterTime(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link Time} straight from the result set.
     */
    @Override
    public Time readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Time convertReadingTarget(Time from) {
        return from;
    }

    /** Renders the time using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(Time to) {
        return target.getDatabaseDateConverter().toDatabaseTime(to);
    }
}
