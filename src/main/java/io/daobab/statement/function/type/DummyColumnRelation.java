package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.query.base.Query;

/**
 * A column expression backed by an inner (sub)query - the query is rendered in parentheses in place of the
 * column (mode {@code INNER_QUERY}). Lets a subquery be used wherever a column function is expected.
 *
 * @param <E> the column's entity
 * @param <F> the column's field type
 * @param <R> the column's relation type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DummyColumnRelation<E extends Entity, F, R extends RelatedTo> extends ColumnFunction<E, F, R, F> {

    private Query<?, ?, ?> query;

    /**
     * @param query  the inner query rendered in place of the column
     * @param column the column whose name/type the expression borrows
     */
    public DummyColumnRelation(Query<?, ?, ?> query, Column<E, F, R> column) {
        super(column, "INNER_QUERY", column.getFieldClass());
        setQuery(query);
        identifier = getColumnName();
    }

    /**
     * The inner query.
     */
    public Query<?, ?, ?> getQuery() {
        return query;
    }

    /** Sets the inner query. */
    public void setQuery(Query<?, ?, ?> query) {
        this.query = query;
    }
}
