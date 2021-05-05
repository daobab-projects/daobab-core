package io.daobab.statement.inner;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface InnerQueryField<E extends Entity, F> {

    InnerSelectManyCells<E, F> innerResult();


}
