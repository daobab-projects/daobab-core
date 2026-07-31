package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for the {@code Void} pseudo type - the counterpart of
 * {@link io.daobab.target.database.converter.type.TypeConverterVoidBased}. A void column (a computed or relation
 * column with no backing database value) never carries a value, so it is always serialized as {@code null} and
 * read back as {@code null}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonVoidConverter extends JsonConverter<Void> {

    @Override
    public void toJson(StringBuilder sb, Void obj) {
        sb.append("null");
    }

    @Override
    public Void fromJson(String json) {
        return null;
    }
}
