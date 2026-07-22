package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterSqlDateBased;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@link java.sql.Date} columns: read straight from the database and written through the
 * target's dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterSqlDate extends TypeConverterSqlDateBased<Date> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterSqlDate(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link java.sql.Date} straight from the result set.
     */
    @Override
    public Date readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Date convertReadingTarget(Date from) {
        return from;
    }

    /** Renders the date using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(Date to) {
        return target.getDatabaseDateConverter().toDatabaseDate(to);
    }
}
