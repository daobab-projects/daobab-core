package io.daobab.statement.condition;

import io.daobab.target.database.query.frozen.DaoParam;

import java.util.HashMap;
import java.util.Map;

/**
 * The row limit (and optional offset) of a query. The limit may be a fixed number or a frozen-query
 * {@link DaoParam} placeholder resolved at execution time.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Limit {

    private static final String LIMIT_KEY = "limit";
    private static final String OFFSET_KEY = "offset";


    private boolean daoParamInUse=false;

    private final Map<String, Object> map = new HashMap<>();

    /**
     * @param limit the maximum number of rows
     */
    public Limit(int limit) {
        setLimit(limit);
    }

    /**
     * @param param a frozen-query parameter supplying the limit at execution time
     */
    public Limit(DaoParam param) {
        setLimit(param);
        param.setAccessibleType(Number.class);
        daoParamInUse=true;
    }

    /**
     * @param from  the offset (number of rows to skip)
     * @param limit the maximum number of rows
     */
    public Limit(int from, int limit) {
        setLimit(from, limit);
    }

    /**
     * The maximum number of rows, or {@code 0} when unset (or a parameter is used).
     */
    public int getLimit() {
        Object val = map.get(LIMIT_KEY);
        if (val == null) return 0;
        return (Integer) val;
    }

    /** Sets the maximum number of rows. */
    public void setLimit(int limit) {
        map.put(LIMIT_KEY, limit);
    }

    /** Sets the limit from a frozen-query parameter. */
    public void setLimit(DaoParam param) {
        map.put(LIMIT_KEY, param);
    }

    /** The frozen-query parameter supplying the limit, or {@code null} when a fixed number is used. */
    public DaoParam getLimitDaoParam() {
        Object val = map.get(LIMIT_KEY);
        if (val instanceof DaoParam){
            return (DaoParam)val;
        }
        return null;
    }

    /** Sets both the offset and the maximum number of rows. */
    public void setLimit(int from, int limit) {
        setLimit(limit);
        setOffset(from);
    }

    /** The offset (number of rows to skip), or {@code 0} when unset. */
    public int getOffset() {
        Object val = map.get(OFFSET_KEY);
        if (val == null) return 0;
        return (Integer) val;
    }

    /** Sets the offset. */
    public void setOffset(int offset) {
        map.put(OFFSET_KEY, offset);
    }

    /** The raw limit/offset map. */
    public Map<String, Object> getLimitMap() {
        return map;
    }


    /** Whether the limit is supplied by a frozen-query parameter rather than a fixed number. */
    public boolean isDaoParamInUse() {
        return daoParamInUse;
    }

}
