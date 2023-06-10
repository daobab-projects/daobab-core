package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.JsonEscape;

public class JsonEnumConverter extends JsonConverter<Enum> {
    @Override
    public void toJson(StringBuilder sb, Enum obj) {
        sb.append(JsonEscape.quote(obj.toString()));
    }

    //TODO:back conversion!
    @Override
    public Enum fromJson(String json) {
        return null;
    }
}
