package io.daobab.statement.function.string;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.function.base.ColumnFunction;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ASCII {

    private ASCII(){}

    public static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> of(Column<E, F, R> column) {
        return new ColumnFunction<>(column, "ASCII", String.class);
    }

    public static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> of(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, "ASCII", clazz);
    }
}
