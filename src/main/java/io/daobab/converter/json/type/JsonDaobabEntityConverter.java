package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.creation.EntityCreator;
import io.daobab.model.Entity;

public class JsonDaobabEntityConverter<E extends Entity> extends JsonConverter<E> {

    private final Class<E> clazz;

    public JsonDaobabEntityConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void toJson(StringBuilder sb, E obj) {
        sb.append(obj.toJson());
    }

    @Override
    public E fromJson(String json) {
        return EntityCreator.createEntityFromJson(clazz, json);
    }
}
