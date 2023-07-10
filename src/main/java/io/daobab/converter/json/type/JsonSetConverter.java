package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class JsonSetConverter extends JsonCollectionBaseConverter<Set> {

    public JsonSetConverter(JsonConverter innerTypeConverter) {
        super(innerTypeConverter);
    }

    @Override
    protected Supplier<Set> collector() {
        return () -> new HashSet();
    }

}
