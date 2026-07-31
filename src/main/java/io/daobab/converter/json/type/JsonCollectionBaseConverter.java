package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Base JSON converter for a {@link java.util.Collection}: writes a JSON array whose elements are each
 * serialized by the {@code innerTypeConverter}, and rebuilds the collection (into the concrete type supplied
 * by {@link #collector()}) on read.
 *
 * @param <C> the concrete collection type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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

    /**
     * Splits a JSON array body into its top-level elements: commas inside quotes or inside a nested
     * object/array ({@code {}}/{@code []}) do not separate elements. This lets a collection hold scalars,
     * quoted strings and nested objects alike (the old {@code \{...}}-only regex dropped every scalar element).
     */
    private static List<String> splitTopLevel(String body) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;
        boolean inQuotes = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (inQuotes) {
                current.append(c);
                if (c == '\\' && i + 1 < body.length()) {
                    current.append(body.charAt(++i)); //keep the escaped character verbatim
                } else if (c == '"') {
                    inQuotes = false;
                }
                continue;
            }
            switch (c) {
                case '"' -> {
                    inQuotes = true;
                    current.append(c);
                }
                case '{', '[' -> {
                    depth++;
                    current.append(c);
                }
                case '}', ']' -> {
                    depth--;
                    current.append(c);
                }
                case ',' -> {
                    if (depth == 0) {
                        parts.add(current.toString());
                        current = new StringBuilder();
                    } else {
                        current.append(c);
                    }
                }
                default -> current.append(c);
            }
        }
        parts.add(current.toString());
        return parts;
    }

    /**
     * Strips the surrounding quotes off a scalar element (mirroring how the entity/plate reader hands the
     * inner, still-escaped payload to a converter); objects and bare numbers are passed through unchanged.
     */
    private static String unwrap(String element) {
        String trimmed = element.trim();
        if (trimmed.length() >= 2 && trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"') {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    @Override
    public C fromJson(String json) {
        String js = json.trim();
        if (!js.startsWith("[") || !js.endsWith("]")) {
            throw new DaobabException("Cannot convert a JSON array");
        }
        js = js.substring(js.indexOf("[") + 1, js.lastIndexOf("]")).trim();

        C result = collector().get();
        if (js.isEmpty()) {
            return result;
        }
        for (String element : splitTopLevel(js)) {
            result.add(innerTypeConverter.fromJson(unwrap(element)));
        }
        return result;
    }

    protected abstract Supplier<C> collector();

}
