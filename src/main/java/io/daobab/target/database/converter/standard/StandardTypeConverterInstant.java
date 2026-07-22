package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterLocalDateTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Standard converter for {@link Instant} columns: stored as a {@code LocalDateTime} at {@link ZoneOffset#UTC}.
 * The database value is read as a {@code LocalDateTime} and interpreted at UTC on both sides.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterInstant extends TypeConverterLocalDateTimeBased<Instant> {

    private final DataBaseTarget target;

    /**
     * @param target the target whose dialect date converter renders the value
     */
    public StandardTypeConverterInstant(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads a {@code LocalDateTime} and interprets it as an {@link Instant} at UTC.
     */
    @Override
    public Instant readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex).toInstant(ZoneOffset.UTC);
    }

    /** Interprets the read {@code LocalDateTime} as an {@link Instant} at UTC. */
    @Override
    public Instant convertReadingTarget(LocalDateTime from) {
        return from.toInstant(ZoneOffset.UTC);
    }

    /** Renders the instant (as a UTC {@code LocalDateTime}) using the target's dialect date converter. */
    @Override
    public String convertWritingTarget(Instant to) {
        return target.getDatabaseDateConverter().toDatabaseLocalDateTime(LocalDateTime.ofInstant(to, ZoneOffset.UTC));
    }

    /** Binds the instant as a UTC {@link LocalDateTime}, or {@code null}. */
    @Override
    public Object convertWritingParameter(Instant to) {
        return to == null ? null : LocalDateTime.ofInstant(to, ZoneOffset.UTC);
    }
}
