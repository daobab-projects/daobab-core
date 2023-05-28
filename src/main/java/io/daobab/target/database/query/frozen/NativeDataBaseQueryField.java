package io.daobab.target.database.query.frozen;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryField;

public final class NativeDataBaseQueryField<E extends Entity,F> extends FrozenDataBaseQueryField<E,F>{
    public NativeDataBaseQueryField(String nativeQuery, QueryTarget target, Column<E, F, ?> column) {
        super(new DataBaseQueryField<>(target,column));
        setSentQuery(nativeQuery);
    }

    @Override
    public DataBaseQueryField<E,F> getOriginalQuery() {
        throw new DaobabException("NativeQuery has no original query to return");
    }
}
