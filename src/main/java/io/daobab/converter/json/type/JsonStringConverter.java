package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.JsonEscape;

public class JsonStringConverter extends JsonConverter<String> {
    @Override
    public void toJson(StringBuilder sb, String obj) {
        sb.append("\"").append(JsonEscape.escape(obj)).append("\"");
    }

    @Override
    public String fromJson(String json) {
        return JsonEscape.unescape(json);
    }
}
