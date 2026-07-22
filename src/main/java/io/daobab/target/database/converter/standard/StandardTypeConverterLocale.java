package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.util.Locale;

/**
 * Standard converter for {@link Locale} columns stored as IETF BCP 47 language tags: the database {@code String}
 * is parsed into a {@code Locale} on reading and rendered back to its language tag on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterLocale extends TypeConverterStringBased<Locale> {

    /**
     * Parses the stored language tag into a {@link Locale}, or {@code null}.
     */
    @Override
    public Locale convertReadingTarget(String from) {
        return from == null ? null : Locale.forLanguageTag(from);
    }

    /** Renders the {@link Locale}'s language tag as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(Locale to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toLanguageTag());
    }

    /** Binds the {@link Locale} as its language tag, or {@code null}. */
    @Override
    public Object convertWritingParameter(Locale to) {
        return to == null ? null : to.toLanguageTag();
    }
}
