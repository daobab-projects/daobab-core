package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes", "java:S1905"})
public abstract class JsonCollectionBaseConverter<C extends Collection> extends JsonConverter<C> {

    final JsonConverter innerTypeConverter;

    protected JsonCollectionBaseConverter(JsonConverter innerTypeConverter) {
        this.innerTypeConverter = innerTypeConverter;
    }

    @Override
    public void toJson(StringBuilder sb, C obj) {
        sb.append("[");
        for (Iterator<?> it = obj.iterator(); it.hasNext(); ) {
            innerTypeConverter.toJson(sb, it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("]");
    }

    @Override
    public C fromJson(String json) {
        String js = json.trim();
        if (!js.startsWith("[") || !js.endsWith("]")) {
            throw new DaobabException("Cannot convert an json array");
        }
        js = js.substring(js.indexOf("[") + 1, js.lastIndexOf("]"));
        Matcher matcher = Pattern.compile("\\{[^}]*\\}").matcher(js);

        List<String> arrays = new ArrayList<>();
        while (matcher.find()) {
            arrays.add(matcher.group());
        }

        if (arrays.isEmpty() && !js.isEmpty()) {
            arrays.add(js);
        }

        return (C) arrays.stream().map(innerTypeConverter::fromJson).collect(Collectors.toCollection(collector()));
    }

    protected abstract Supplier<C> collector();

}
