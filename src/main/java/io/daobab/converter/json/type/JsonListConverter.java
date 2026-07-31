package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * JSON converter for a {@link java.util.List}: a {@link JsonCollectionBaseConverter} that collects the
 * parsed elements into an {@link java.util.ArrayList}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonListConverter extends JsonCollectionBaseConverter<List> {

    public JsonListConverter(JsonConverter innerTypeConverter) {
        super(innerTypeConverter);
    }

    @Override
    protected Supplier<List> collector() {
        return () -> new ArrayList<>();
    }

}
