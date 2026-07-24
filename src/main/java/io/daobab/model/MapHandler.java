package io.daobab.model;

import java.util.Map;

/**
 * The parameter-map backing of an entity: the shared column interfaces read and write their value through
 * {@link #readParam(String)} / {@link #storeParam(String, Object)} against the entity's underlying
 * {@code String -> Object} map. This is what lets one column interface be reused across many entities.
 *
 * @param <E> the entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface MapHandler<E extends Entity> {

    /**
     * Reads the value stored under the key.
     */
    <X> X readParam(String key);

    /** Stores the value under the key and returns the entity for chaining. */
    <X> E storeParam(String key, X param);

    /** The underlying parameter map. */
    Map<String, Object> accessParameterMap();
}
