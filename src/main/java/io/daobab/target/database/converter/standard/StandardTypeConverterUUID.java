package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.util.UUID;

/**
 * Standard converter for {@link UUID} columns stored as strings: the database {@code String} is parsed into a
 * {@code UUID} on reading and rendered back to its canonical text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterUUID extends TypeConverterStringBased<UUID> {

    /**
     * Parses the stored string into a {@link UUID}, or {@code null}.
     */
    @Override
    public UUID convertReadingTarget(String from) {
        if (from == null) {
            return null;
        }
        return UUID.fromString(from);
    }

    /** Renders the {@link UUID} as its canonical text, or {@code null}. */
    @Override
    public String convertWritingTarget(UUID to) {
        return to == null ? null : String.valueOf(to);
    }

    /** Binds the {@link UUID} as its string form, or {@code null}. */
    @Override
    public Object convertWritingParameter(UUID to) {
        return to == null ? null : String.valueOf(to);
    }
}
