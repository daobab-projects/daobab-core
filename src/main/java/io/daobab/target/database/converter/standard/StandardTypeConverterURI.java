package io.daobab.target.database.converter.standard;

import io.daobab.error.DaobabException;
import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Standard converter for {@link URI} columns stored as strings: the database {@code String} is parsed into a
 * {@code URI} on reading and rendered back to its text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterURI extends TypeConverterStringBased<URI> {

    /**
     * Parses the stored string into a {@link URI}, or {@code null}.
     *
     * @throws DaobabException if the string is not a valid URI
     */
    @Override
    public URI convertReadingTarget(String from) {
        if (from == null) {
            return null;
        }
        try {
            return new URI(from);
        } catch (URISyntaxException e) {
            throw new DaobabException("Problem during URI conversion", e);
        }
    }

    /**
     * Renders the {@link URI} as a quoted SQL literal, or {@code null}.
     */
    @Override
    public String convertWritingTarget(URI to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link URI} as its string form, or {@code null}. */
    @Override
    public Object convertWritingParameter(URI to) {
        return to == null ? null : to.toString();
    }
}
