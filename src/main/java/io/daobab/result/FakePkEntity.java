package io.daobab.result;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class FakePkEntity<F extends Number, E extends Entity> {

    private F pk;
    private E entity;

    public FakePkEntity(F pk, E entity) {
        this.setPk(pk);
        this.setEntity(entity);
    }

    public F getPk() {
        return pk;
    }

    public void setPk(F pk) {
        this.pk = pk;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }
}
