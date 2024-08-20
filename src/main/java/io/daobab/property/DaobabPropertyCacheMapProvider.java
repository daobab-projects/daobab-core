package io.daobab.property;

import java.util.HashMap;
import java.util.Map;

public abstract class DaobabPropertyCacheMapProvider {

    private final Map<String, Object> cacheMap = new HashMap<>();

    public Map<String, Object> getCacheMap() {
        return cacheMap;
    }

    protected abstract String getRootPath();
}
