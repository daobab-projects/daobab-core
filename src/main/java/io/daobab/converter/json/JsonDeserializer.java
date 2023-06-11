package io.daobab.converter.json;

import io.daobab.clone.EntityDuplicator;
import io.daobab.converter.json.conversion.EntityJsonConversion;
import io.daobab.model.Entity;

import java.util.HashMap;

public class JsonDeserializer extends HashMap<String, String> {

    public <E extends Entity> E deserializeEntity(Class<E> entityClass) {
        E entity = EntityDuplicator.createEntity(entityClass);
        EntityJsonConversion<E> jsonConversion = JsonConverterManager.INSTANCE.getEntityJsonConverter(entity);
        return jsonConversion.deserialize(entity, this);
    }
}
