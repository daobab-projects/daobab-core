package io.daobab.statement.function.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

public class SubstringColumnRelation<E extends Entity, F, R extends EntityRelation, C> extends ColumnFunction<E, F, R, C> {

    public Integer from;
    public Integer to;
    public Column fromColumn;
    public Column toColumn;

    public SubstringColumnRelation(Column<E, F, R> column, String mode, Class<C> functionClass, int from, int to) {
        super(column, mode, functionClass);
        this.from = from;
        this.to = to;
    }

    public SubstringColumnRelation(Column<E, F, R> column, String mode, Class<C> functionClass, Column<?, Number, ?> fromColumn, int to) {
        super(column, mode, functionClass);
        this.fromColumn = fromColumn;
        this.to = to;
    }

    public SubstringColumnRelation(Column<E, F, R> column, String mode, Class<C> functionClass, int from, Column<?, Number, ?> toColumn) {
        super(column, mode, functionClass);
        this.toColumn = toColumn;
        this.from = from;
    }

    public SubstringColumnRelation(Column<E, F, R> column, String mode, Class<C> functionClass, Column<?, Number, ?> fromColumn, Column<?, Number, ?> toColumn) {
        super(column, mode, functionClass);
        this.toColumn = toColumn;
        this.fromColumn = fromColumn;
    }

}
