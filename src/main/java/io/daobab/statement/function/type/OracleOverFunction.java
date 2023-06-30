package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class OracleOverFunction<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    public OracleOverFunction(String mode, Class<C> functionClass) {
        super(mode, functionClass);
    }

    public OracleOverFunction<E, F, R, C> over(Column<?, ?, ?> partitionBy) {
        return this;
    }
}
