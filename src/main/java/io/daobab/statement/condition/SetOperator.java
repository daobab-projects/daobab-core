package io.daobab.statement.condition;

import io.daobab.query.base.Query;

public class SetOperator {

    public static final int UNION = 0;
    public static final int UNION_ALL = 1;
    public static final int EXCEPT = 2;
    public static final int EXCEPT_ALL = 3;
    public static final int INTERSECT = 4;
    public static final int MINUS = 5;

    public Query<?,?,?> getQuery() {
        return query;
    }

    public void setQuery(Query<?,?,?> query) {
        this.query = query;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Query<?,?,?> query;
    private int type = UNION;

    public SetOperator(int type, Query<?,?,?> query) {
        setType(type);
        setQuery(query);
    }


}
