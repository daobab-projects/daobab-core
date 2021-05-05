package io.daobab.statement.condition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class Limit {


    private static final String LIMIT_KEY = "limit";
    private static final String OFFSET_KEY = "offset";

    Map<String, Object> map = new HashMap<>();

    public Limit(int limit) {
        setLimit(limit);
    }

    public Limit(int from, int limit) {
        setLimit(from, limit);
    }

    public int getLimit() {
        Object val = map.get(LIMIT_KEY);
        if (val == null) return 0;
        return (Integer) val;
    }

    public void setLimit(int limit) {
        map.put(LIMIT_KEY, limit);
    }

    public void setLimit(int from, int limit) {
        setLimit(limit);
        setOffset(from);
    }

    public int getOffset() {
        Object val = map.get(OFFSET_KEY);
        if (val == null) return 0;
        return (Integer) val;
    }

    public void setOffset(int offset) {
        map.put(OFFSET_KEY, offset);
    }

    public Map<String, Object> getLimitMap() {
        return map;
    }

}
