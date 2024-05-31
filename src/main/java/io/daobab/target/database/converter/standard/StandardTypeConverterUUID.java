package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.util.UUID;

public class StandardTypeConverterUUID extends TypeConverterStringBased<UUID> {

    @Override
    public UUID convertReadingTarget(String from) {
        if (from == null) {
            return null;
        }
        return UUID.fromString(from);
    }

    @Override
    public String convertWritingTarget(UUID to) {
        return to == null ? null : String.valueOf(to);
    }
}
