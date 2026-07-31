package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * JSON converter for a {@link java.util.Set}: a {@link JsonCollectionBaseConverter} that collects the
 * parsed elements into a {@link java.util.HashSet}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonSetConverter extends JsonCollectionBaseConverter<Set> {

    public JsonSetConverter(JsonConverter innerTypeConverter) {
        super(innerTypeConverter);
    }

    @Override
    protected Supplier<Set> collector() {
        return () -> new HashSet();
    }

}
