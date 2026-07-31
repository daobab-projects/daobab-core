package io.daobab.converter.json;

/**
 * The two-way JSON contract for a single value type {@code F}: {@link #toJson} appends the value's JSON form to
 * the given builder (string-like values wrapped in quotes, numbers/booleans bare), and {@link #fromJson} rebuilds
 * the value from the already-unquoted payload. Implemented by every converter in
 * {@code io.daobab.converter.json.type} through the {@link JsonConverter} base.
 *
 * @param <F> the value type this converter (de)serializes
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface JsonType<F> {

    /**
     * Appends the JSON representation of {@code obj} to {@code sb}.
     */
    void toJson(StringBuilder sb, F obj);

    /**
     * Rebuilds the value from its JSON payload (already stripped of the surrounding quotes for string-like types).
     */
    F fromJson(String json);
}
