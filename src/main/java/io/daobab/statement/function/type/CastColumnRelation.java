package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.statement.function.base.CastType;

/**
 * A {@code CAST(column AS type)} function expression: a {@link ColumnFunction} that additionally carries the
 * target {@link CastType}.
 *
 * @param <E> the column's entity
 * @param <F> the column's field type
 * @param <R> the column's relation type
 * @param <C> the result type of the cast
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CastColumnRelation<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    /**
     * The target type of the cast.
     */
    public final CastType type;

    /**
     * @param column        the column being cast
     * @param mode          the SQL function name ({@code CAST})
     * @param type          the target type
     * @param functionClass the Java result type
     */
    public CastColumnRelation(Column<E, F, R> column, String mode, CastType type, Class<C> functionClass) {
        super(column, mode, functionClass);
        this.type = type;
    }

}
