package io.daobab.statement.condition;

import io.daobab.error.MandatoryColumn;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SetField<E extends Entity> {
    E entity;
    private Column<E, Object, ?> field;
    private Object value;

    public <R extends RelatedTo> SetField(Column<E, Object, R> field, R related) {
        if (field == null) {
            throw new MandatoryColumn();
        }
        this.setField(field);
        this.setValue(related == null ? null : field.getValue(related));
        this.entity = field.getInstance();
    }

    public SetField(Column<E, Object, ?> field, Object value) {
        if (field == null) {
            throw new MandatoryColumn();
        }
        this.setField(field);
        this.setValue(value);
        this.entity = field.getInstance();
    }

    public Column<E, Object, ?> getField() {
        return field;
    }

    public void setField(Column<E, Object, ?> field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
