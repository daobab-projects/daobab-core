package io.daobab.statement.inner;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

/**
 * Retrieves inner queries from entities related Queries
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface InnerQueryEntity<E extends Entity> {

    @SuppressWarnings("rawtypes")
    <E1 extends Entity, F, R extends EntityRelation> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> field);

    String getEntityName();
}
