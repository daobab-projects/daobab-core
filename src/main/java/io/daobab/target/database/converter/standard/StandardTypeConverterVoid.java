package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterVoidBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for pseudo columns that have no database value of their own (computed or relation
 * columns): it reads and writes nothing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterVoid extends TypeConverterVoidBased<Void> {

    private final DataBaseTarget target;

    /**
     * @param target the target this converter belongs to
     */
    public StandardTypeConverterVoid(DataBaseTarget target) {
        this.target = target;
    }

    /**
     * Reads nothing (there is no backing column); returns {@code null}.
     */
    @Override
    public Void readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    /** Returns {@code null}: there is nothing to convert. */
    @Override
    public Void convertReadingTarget(Void from) {
        return from;
    }

    /** Writes nothing; returns {@code null}. */
    @Override
    public String convertWritingTarget(Void to) {
        return null;
    }
}
