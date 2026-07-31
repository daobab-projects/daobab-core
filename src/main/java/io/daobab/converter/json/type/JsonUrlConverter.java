package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * JSON converter for {@link java.net.URL}: written as quoted URL text and parsed with the
 * {@code URL(String)} constructor.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonUrlConverter extends JsonConverter<URL> {
    @Override
    public void toJson(StringBuilder sb, URL obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public URL fromJson(String json) {
        try {
            return new URL(json);
        } catch (MalformedURLException e) {
            throw new DaobabException("Problem during Json conversion", e);
        }
    }
}
