package io.daobab.statement.function.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

public class CastColumnRelation<E extends Entity, F, R extends EntityRelation, C> extends ColumnFunction<E, F, R, C> {

    public final CastType type;

    public CastColumnRelation(Column<E, F, R> column, String mode, CastType type, Class<C> functionClass) {
        super(column, mode, functionClass);
        this.type = type;
    }

}
