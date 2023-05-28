package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.statement.function.base.CastType;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CastColumnRelation<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    public final CastType type;

    public CastColumnRelation(Column<E, F, R> column, String mode, CastType type, Class<C> functionClass) {
        super(column, mode, functionClass);
        this.type = type;
    }

}
