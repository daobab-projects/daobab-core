package io.daobab.statement.function.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.Query;
import io.daobab.statement.DictFunction;

public class DummyColumnRelation<E extends Entity, F, R extends EntityRelation> extends ColumnFunction<E, F, R, F> {

    private Query<?, ?> query;

    public DummyColumnRelation(Query<?, ?> query, Column<E, F, R> column) {
        super(column, DictFunction.INNER_QUERY, column.getFieldClass());
        setQuery(query);
        identifier = getColumnName();
    }

    public void setQuery(Query<?, ?> query) {
        this.query = query;
    }

    public Query<?, ?> getQuery() {
        return query;
    }
}
