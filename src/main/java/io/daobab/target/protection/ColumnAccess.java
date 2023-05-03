package io.daobab.target.protection;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnAccess {

    private Entity entity;
    private Access accessRight;

    public ColumnAccess(Entity entity, Access accessRight) {
        setEntity(entity);
        setAccessRight(accessRight);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Access getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(Access accessRight) {
        this.accessRight = accessRight;
    }
}
