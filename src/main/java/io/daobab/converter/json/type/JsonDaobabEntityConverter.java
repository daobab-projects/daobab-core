package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.creation.EntityCreator;
import io.daobab.model.Entity;

/**
 * JSON converter for a daobab {@link io.daobab.model.Entity}: delegates to the entity's own
 * {@code toJson()} and rebuilds it via {@code EntityCreator.createEntityFromJson}.
 *
 * @param <E> the entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
