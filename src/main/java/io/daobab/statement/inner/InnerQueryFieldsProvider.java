package io.daobab.statement.inner;

import io.daobab.model.Entity;

/**
 * Supplies the single-field inner query used as a subquery expression in a where/having clause (e.g.
 * {@code whereIn(column, provider)}). Implemented by the field queries and by a buffered field list.
 *
 * @param <E> the entity of the inner query
 * @param <F> the type of the projected field
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface InnerQueryFieldsProvider<E extends Entity, F> {

    /**
     * The inner query (or buffered fields) rendered in place of the expression.
     */
    InnerQueryFields<E, F> innerResult();

}
