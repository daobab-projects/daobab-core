package io.daobab.query.base;

import io.daobab.model.Entity;
import io.daobab.target.database.query.DataBaseQueryBase;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface QueryExpressionProvider<E extends Entity> {

    DataBaseQueryBase<E, ?> getInnerQuery();

}
