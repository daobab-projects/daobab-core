package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * JSON converter for {@link java.net.URI}: written as quoted URI text and parsed with the
 * {@code URI(String)} constructor.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonUriConverter extends JsonConverter<URI> {

    @Override
    public void toJson(StringBuilder sb, URI obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public URI fromJson(String json) {
        try {
            return new URI(json);
        } catch (URISyntaxException e) {
            throw new DaobabException("Problem during Json conversion", e);
        }
    }
}
