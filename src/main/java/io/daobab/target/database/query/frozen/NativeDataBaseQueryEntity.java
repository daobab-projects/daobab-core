package io.daobab.target.database.query.frozen;

import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryEntity;

public final class NativeDataBaseQueryEntity<E extends Entity> extends FrozenDataBaseQueryEntity<E>{
    public NativeDataBaseQueryEntity(String nativeQuery, QueryTarget target,E entity) {
        super(new DataBaseQueryEntity<>(target,entity));
        setSentQuery(nativeQuery);
    }

    @Override
    public DataBaseQueryEntity<E> getOriginalQuery() {
        throw new DaobabException("NativeQuery has no original query to return");
    }
}
