package io.daobab.target.buffer.multi;

import io.daobab.model.Entity;
import io.daobab.target.database.query.DataBaseQueryEntity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface QueryMultiEntity extends MultiEntity {

    <E extends Entity> void register(DataBaseQueryEntity<E> refreshQuery);

    <E extends Entity> void refresh(Class<E> entityClazz);

    void refreshAll();
}
