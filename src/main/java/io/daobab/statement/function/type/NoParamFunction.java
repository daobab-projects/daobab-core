package io.daobab.statement.function.type;

import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * A function taking no argument, e.g. {@code PI()}, {@code NOW()} or {@code CURRENT_DATE} - rendered as the
 * bare function name with empty parentheses.
 *
 * @param <E> the entity
 * @param <F> the field type
 * @param <R> the relation type
 * @param <C> the result type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoParamFunction<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    /**
     * @param mode          the SQL function name
     * @param functionClass the Java result type
     */
    public NoParamFunction(String mode, Class<C> functionClass) {
        super(mode, functionClass);
        setNoParameter(true);
    }

    /**
     * @param mode the SQL function name
     */
    public NoParamFunction(String mode) {
        super(mode);
        setNoParameter(true);
    }
}
