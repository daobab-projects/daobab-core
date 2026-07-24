package io.daobab.statement.inner;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * An entity subquery that can be narrowed to one of its columns to become a single-field subquery expression
 * (e.g. {@code whereIn(column, entitySubquery)} projects the subquery to {@code column}).
 *
 * @param <E> the entity of the inner query
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface InnerQueryEntity<E extends Entity> {

    /**
     * Narrows this entity subquery to the given column, yielding a single-field subquery provider.
     *
     * @param field the column to project the subquery onto
     * @return the single-field subquery provider
     */
    @SuppressWarnings("rawtypes")
    <E1 extends Entity, F, R extends RelatedTo> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> field);

    /**
     * The name of the subquery's entity.
     */
    String getEntityName();
}
