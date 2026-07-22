package io.daobab.query.base;

import io.daobab.model.Entity;
import io.daobab.target.database.query.DataBaseQueryBase;

/**
 * Supplies the inner query that backs a value used as a subquery expression inside a where or having clause
 * (e.g. {@code whereIn(column, someInnerQuery)}).
 *
 * @param <E> the entity of the inner query
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface QueryExpressionProvider<E extends Entity> {

    /**
     * The inner (sub)query rendered in place of the expression.
     */
    DataBaseQueryBase<E, ?> getInnerQuery();

}
