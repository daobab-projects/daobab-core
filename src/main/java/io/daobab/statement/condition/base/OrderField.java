package io.daobab.statement.condition.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("rawtypes")
public class OrderField<E extends Entity, F, R extends EntityRelation> {

    private Column<E, F, R> field;
    private String direction;

    public OrderField(Column<E, F, R> field, String direction) {
        setField(field);
        setDirection(direction);
    }

    public Column<E, F, R> getField() {
        return field;
    }

    public void setField(Column<E, F, R> field) {
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
