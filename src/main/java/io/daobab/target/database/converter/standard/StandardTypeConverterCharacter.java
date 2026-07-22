package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

/**
 * Standard converter for {@link Character} columns stored as one-character strings: the first character of the
 * database {@code String} is taken on reading, and the character is rendered back as a quoted literal on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterCharacter extends TypeConverterStringBased<Character> {

    /**
     * Returns the first character of the stored string, or {@code null} when it is null or empty.
     */
    @Override
    public Character convertReadingTarget(String from) {
        if (from == null || from.isEmpty()) {
            return null;
        }
        return from.charAt(0);
    }

    /** Renders the {@link Character} as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(Character to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : String.valueOf(to));
    }

    /** Binds the {@link Character} as a one-character string, or {@code null}. */
    @Override
    public Object convertWritingParameter(Character to) {
        return to == null ? null : String.valueOf(to);
    }
}
