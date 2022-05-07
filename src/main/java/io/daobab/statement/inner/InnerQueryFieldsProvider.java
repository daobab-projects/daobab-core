package io.daobab.statement.inner;

import io.daobab.model.Entity;

/**
 * Indicates an object, which may provide, either a complete inner query or fields
 * which may be used later into inner query
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface InnerQueryFieldsProvider<E extends Entity, F> {

    InnerQueryFields<E, F> innerResult();

}
