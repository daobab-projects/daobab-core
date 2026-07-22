package io.daobab.statement.condition;

import io.daobab.error.MandatoryColumn;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * A single {@code column = value} assignment for an update or an insert. Built fluently through
 * {@link io.daobab.query.base.QueryWhisperer#set(Column, Object)}.
 *
 * @param <E> the entity the column belongs to
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SetField<E extends Entity> {
    E entity;
    private Column<E, Object, ?> field;
    private Object value;

    /**
     * Assigns the column the value it holds in the given related entity.
     *
     * @param field   the assigned column
     * @param related the related entity the value is read from (may be {@code null})
     */
    public <R extends RelatedTo> SetField(Column<E, Object, R> field, R related) {
        if (field == null) {
            throw new MandatoryColumn();
        }
        this.setField(field);
        this.setValue(related == null ? null : field.getValue(related));
        this.entity = field.getInstance();
    }

    /**
     * Assigns the column a plain value.
     *
     * @param field the assigned column
     * @param value the value
     */
    public SetField(Column<E, Object, ?> field, Object value) {
        if (field == null) {
            throw new MandatoryColumn();
        }
        this.setField(field);
        this.setValue(value);
        this.entity = field.getInstance();
    }

    /**
     * The assigned column.
     */
    public Column<E, Object, ?> getField() {
        return field;
    }

    /** Sets the assigned column. */
    public void setField(Column<E, Object, ?> field) {
        this.field = field;
    }

    /** The assigned value. */
    public Object getValue() {
        return value;
    }

    /** Sets the assigned value. */
    public void setValue(Object value) {
        this.value = value;
    }
}
