package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterUtilDateBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Standard converter for {@link java.util.Date} columns: read straight from the database, written through the
 * target's dialect date converter and bound as a {@link java.sql.Timestamp} parameter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterUtilDate extends TypeConverterUtilDateBased<Date> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterUtilDate(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the date straight from the result set.
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

    /** Binds the value as a {@link java.sql.Timestamp}, or {@code null}. */
    @Override
    public Object convertWritingParameter(Date to) {
        return to == null ? null : new java.sql.Timestamp(to.getTime());
    }
}
