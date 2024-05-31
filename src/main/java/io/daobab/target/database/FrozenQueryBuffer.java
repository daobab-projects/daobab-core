package io.daobab.target.database;

import io.daobab.target.database.query.frozen.FrozenDataBaseQueryBase;

import java.util.HashMap;
import java.util.Map;

public class FrozenQueryBuffer {

    private final Map<String, FrozenDataBaseQueryBase<?, ?, ?>> cache;

    public FrozenQueryBuffer() {
        cache = new HashMap<>();
    }

    public void putFrozenQuery(String query, FrozenDataBaseQueryBase<?, ?, ?> frozenQuery) {
        cache.put(query, frozenQuery);
    }

    public Map<String, FrozenDataBaseQueryBase<?, ?, ?>> getBuffer() {
        return cache;
    }
}
