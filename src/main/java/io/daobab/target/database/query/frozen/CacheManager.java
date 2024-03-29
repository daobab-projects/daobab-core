package io.daobab.target.database.query.frozen;

import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CacheManager {

    private final Map<String, CachedObject> manyResultsCache = new ConcurrentHashMap<>();
    private final Map<String, CachedObject> singleResultCache = new ConcurrentHashMap<>();
    private final Map<String, CachedObject> singleResultJsonCache = new ConcurrentHashMap<>();
    private final Map<String, CachedObject> manyResultJsonCache = new ConcurrentHashMap<>();

    public <X> X getManyContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery, period, reader, manyResultsCache);
    }

    public <X> X getSingleContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery, period, reader, singleResultCache);
    }

    public <X> X getSingleJsonContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery, period, reader, singleResultJsonCache);
    }

    public <X> X getManyJsonContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery, period, reader, manyResultJsonCache);
    }

    <X> X refreshIfNeeded(String sqlQuery, TemporalAmount period, Supplier<X> reader, Map<String, CachedObject> map) {
        CachedObject value = map.computeIfAbsent(sqlQuery, s -> new CachedObject(s, period, reader.get()));
        if (value.needRefresh()) {
            CachedObject newValue = new CachedObject(sqlQuery, period, reader.get());
            map.put(sqlQuery, newValue);
            return newValue.getContent();
        }
        return value.getContent();
    }
}
