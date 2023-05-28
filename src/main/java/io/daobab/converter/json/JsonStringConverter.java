package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.JsonEscape;

public class JsonStringConverter implements JsonConverter<String> {
    @Override
    public String toJson(String obj) {
        return JsonEscape.quote(obj);
    }

    //TODO:back conversion!
    @Override
    public String fromJson(String json) {
        return JsonEscape.quote(json);
    }
}
