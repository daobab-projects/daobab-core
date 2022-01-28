package io.daobab.target.database.query.frozen;

import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CacheManager {

    private final Map<String, CachedObject> manyResultsCache = new ConcurrentHashMap<>();
    private final Map<String, CachedObject> singleResultCache = new ConcurrentHashMap<>();
    private final Map<String, CachedObject> countCache = new ConcurrentHashMap<>();

    public <X> X getManyContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery,period,reader,manyResultsCache);
    }

    public <X> X getSingleContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery,period,reader,singleResultCache);
    }

    public <X> X getCountContent(String sqlQuery, TemporalAmount period, Supplier<X> reader) {
        return refreshIfNeeded(sqlQuery,period,reader,countCache);
    }

    <X> X refreshIfNeeded(String sqlQuery, TemporalAmount period, Supplier<X> reader, Map<String, CachedObject> map) {
        CachedObject value = map.computeIfAbsent(sqlQuery, s -> new CachedObject(s, period, reader.get()));
        if (value.needRefresh()){
            CachedObject newValue=new CachedObject(sqlQuery, period, reader.get());
            map.put(sqlQuery,newValue);
            return newValue.getContent();
        }
        return value.getContent();
    }
}
