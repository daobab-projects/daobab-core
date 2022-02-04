package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.Query;

public class DummyColumnRelation<E extends Entity, F, R extends EntityRelation> extends ColumnFunction<E, F, R, F> {

    private Query<?, ?, ?> query;

    public DummyColumnRelation(Query<?, ?, ?> query, Column<E, F, R> column) {
        super(column, "INNER_QUERY", column.getFieldClass());
        setQuery(query);
        identifier = getColumnName();
    }

    public Query<?, ?, ?> getQuery() {
        return query;
    }

    public void setQuery(Query<?, ?, ?> query) {
        this.query = query;
    }
}
