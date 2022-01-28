package io.daobab.statement.condition;

import io.daobab.target.database.query.frozen.DaoParam;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Limit {

    private static final String LIMIT_KEY = "limit";
    private static final String OFFSET_KEY = "offset";


    private boolean daoParamInUse=false;

    private final Map<String, Object> map = new HashMap<>();

    public Limit(int limit) {
        setLimit(limit);
    }
    public Limit(DaoParam param) {
        setLimit(param);
        param.setAccessibleType(Number.class);
        daoParamInUse=true;
    }

    public Limit(int from, int limit) {
        setLimit(from, limit);
    }

    public int getLimit() {
        Object val = map.get(LIMIT_KEY);
        if (val == null) return 0;
        return (Integer) val;
    }

    public DaoParam getLimitDaoParam() {
        Object val = map.get(LIMIT_KEY);
        if (val instanceof DaoParam){
            return (DaoParam)val;
        }
        return null;
    }

    public void setLimit(int limit) {
        map.put(LIMIT_KEY, limit);
    }
    public void setLimit(DaoParam param) {
        map.put(LIMIT_KEY, param);
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


    public boolean isDaoParamInUse() {
        return daoParamInUse;
    }

}
