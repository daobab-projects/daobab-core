package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;
import io.daobab.error.DaobabException;

import java.net.MalformedURLException;
import java.net.URL;

public class JsonUrlConverter extends JsonConverter<URL> {
    @Override
    public void toJson(StringBuilder sb, URL obj) {
        sb.append(obj.toString());
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
