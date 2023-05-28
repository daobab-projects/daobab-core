package io.daobab.target.database.query.frozen;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryEntity;
import io.daobab.target.database.query.DataBaseQueryPlate;

public final class NativeDataBaseQueryPlate extends FrozenDataBaseQueryPlate{
    public NativeDataBaseQueryPlate(String nativeQuery, QueryTarget target, Column<? extends Entity, ?, ?>[] columns) {
        super(new DataBaseQueryPlate(target,columns));
        setSentQuery(nativeQuery);
    }

    @Override
    public DataBaseQueryPlate getOriginalQuery() {
        throw new DaobabException("NativeQuery has no original query to return");
    }
}
