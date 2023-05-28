package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class JsonCollectionConverter extends JsonCollectionBaseConverter<Collection> {

    public JsonCollectionConverter(JsonConverter innerTypeConverter) {
        super(innerTypeConverter);
    }

    @Override
    protected Supplier<Collection> collector() {
        return () -> new ArrayList();
    }

}
