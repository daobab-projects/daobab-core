package io.daobab.model;

import io.daobab.converter.json.JsonConverterManager;

import java.util.HashMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Table extends HashMap<String, Object> implements EntityMap {

    @Override
    public String toJson() {
        return JsonConverterManager.INSTANCE.getEntityJsonConverter(this).toJson(new StringBuilder(),this).toString();
    }
}
