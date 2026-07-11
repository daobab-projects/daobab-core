package io.daobab.target.database.converter.standard;

import io.daobab.error.DaobabException;
import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.net.URI;
import java.net.URISyntaxException;

public class StandardTypeConverterURI extends TypeConverterStringBased<URI> {

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

    @Override
    public String convertWritingTarget(URI to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(URI to) {
        return to == null ? null : to.toString();
    }
}
