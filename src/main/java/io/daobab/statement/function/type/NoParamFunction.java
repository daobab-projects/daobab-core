package io.daobab.statement.function.type;

import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoParamFunction<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    public NoParamFunction(String mode, Class<C> functionClass) {
        super(mode, functionClass);
        setNoParameter(true);
    }

    public NoParamFunction(String mode) {
        super(mode);
        setNoParameter(true);
    }
}
