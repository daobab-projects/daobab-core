package io.daobab.query.base;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface QueryExpressionProvider<E extends Entity,Q extends Query>{

    Query<E,?,Q> getSelect();

    boolean isResultCached();
}
