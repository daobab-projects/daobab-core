package io.daobab.statement.inner;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface InnerQueryEntity<E extends Entity> {

    InnerSelectManyEntities<E> innerResult();

    <E1 extends Entity, F, R extends EntityRelation> InnerQueryField<E1, F> limitToField(Column<E1, F, R> field);

    String getEntityName();
}
