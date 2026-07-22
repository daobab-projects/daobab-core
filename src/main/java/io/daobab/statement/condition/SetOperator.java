package io.daobab.statement.condition;

import io.daobab.query.base.Query;

/**
 * A set operation combining a query with another one ({@code UNION}, {@code INTERSECT}, ...); the {@link #type}
 * is one of the constants below. Added to a query through the {@code union}/{@code intersect}/... shortcuts of
 * {@link io.daobab.query.base.QuerySetOperator}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SetOperator {

    /**
     * {@code UNION} (distinct rows).
     */
    public static final int UNION = 0;
    /** {@code UNION ALL} (keeps duplicates). */
    public static final int UNION_ALL = 1;
    /** {@code EXCEPT} (rows of the first query not in the second). */
    public static final int EXCEPT = 2;
    /** {@code EXCEPT ALL} (keeps duplicates). */
    public static final int EXCEPT_ALL = 3;
    /** {@code INTERSECT} (rows present in both). */
    public static final int INTERSECT = 4;
    /** {@code MINUS} (the Oracle synonym of {@code EXCEPT}). */
    public static final int MINUS = 5;
    private Query<?, ?, ?> query;
    private int type = UNION;

    /**
     * @param type  one of the operator constants ({@link #UNION}, ...)
     * @param query the query combined with the owning one
     */
    public SetOperator(int type, Query<?, ?, ?> query) {
        setType(type);
        setQuery(query);
    }

    /** The query combined with the owning one. */
    public Query<?, ?, ?> getQuery() {
        return query;
    }

    /** Sets the combined query. */
    public void setQuery(Query<?, ?, ?> query) {
        this.query = query;
    }

    /** The operator type (one of the constants). */
    public int getType() {
        return type;
    }

    /** Sets the operator type. */
    public void setType(int type) {
        this.type = type;
    }

}
