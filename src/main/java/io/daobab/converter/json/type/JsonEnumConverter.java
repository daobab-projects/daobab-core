package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

public class JsonEnumConverter<E extends Enum<E>> extends JsonConverter<E> {

    private final Class<E> clazz;
    private final JsonStringConverter jsonStringConverter;

    public JsonEnumConverter(Class<E> clazz) {
        this.clazz = clazz;
        jsonStringConverter = new JsonStringConverter();
    }

    @Override
    public void toJson(StringBuilder sb, E obj) {
        jsonStringConverter.toJson(sb, obj.name());
    }

    @Override
    public E fromJson(String json) {
        return Enum.valueOf(clazz, json);
    }
}
