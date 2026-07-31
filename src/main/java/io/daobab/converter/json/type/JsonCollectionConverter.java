package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * JSON converter for a plain {@link java.util.Collection}: a {@link JsonCollectionBaseConverter} that
 * collects the parsed elements into an {@link java.util.ArrayList}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonCollectionConverter extends JsonCollectionBaseConverter<Collection> {

    public JsonCollectionConverter(JsonConverter innerTypeConverter) {
        super(innerTypeConverter);
    }

    @Override
    protected Supplier<Collection> collector() {
        return () -> new ArrayList();
    }

}
