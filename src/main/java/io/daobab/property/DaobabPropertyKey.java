package io.daobab.property;


import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DaobabPropertyKey<T> {

    private final String key;
    private final Function<String, T> readFunction;
    private final BiFunction<String, String, T> readFunctionDefault;
    private final boolean hasDefault;
    private final String defaultValue;
    private final Map<String, Object> cacheMap;

    public DaobabPropertyKey(final DaobabPropertyCacheMapProvider cacheMap, final String key, final Function<String, T> readFunction) {
        this.key = key;
        this.readFunction = readFunction;
        this.hasDefault = false;
        this.defaultValue = "";
        this.readFunctionDefault = (x, y) -> null;
        this.cacheMap = cacheMap.getCacheMap();
    }

    public DaobabPropertyKey(final DaobabPropertyCacheMapProvider cacheMap, final String key, final String defaultValue, final BiFunction<String, String, T> readFunction) {
        this.key = cacheMap.getRootPath() + key;
        this.readFunction = s -> null;
        this.readFunctionDefault = readFunction;
        this.defaultValue = defaultValue;
        this.hasDefault = !(defaultValue == null || defaultValue.isEmpty());
        this.cacheMap = cacheMap.getCacheMap();
    }

    public String getKey() {
        return this.key;
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) cacheMap.computeIfAbsent(this.key, k -> hasDefault ? readFunctionDefault.apply(k, defaultValue) : readFunction.apply(k));
    }

}
