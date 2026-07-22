package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Standard converter for {@link LocalDate} columns: read straight from the database (through a
 * {@code java.sql.Date}) and written through the target's dialect date converter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterLocalDate extends TypeConverterLocalDateBased<LocalDate> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterLocalDate(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads the {@link LocalDate} straight from the result set.
     */
    @Override
    public LocalDate readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public LocalDate convertReadingTarget(LocalDate from) {
        return from;
    }

    /** Renders the date using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(LocalDate to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDate(to);
    }
}
