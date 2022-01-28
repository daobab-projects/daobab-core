package io.daobab.target.protection;

import io.daobab.model.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EntityAccess {

    private Entity entity;
    private Set<Access> accessRights = new HashSet<>();

    public EntityAccess(Entity entity, Access... accessRights) {
        setEntity(entity);

        this.accessRights.addAll(Arrays.asList(accessRights));
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }


    public Set<Access> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Set<Access> accessRights) {
        this.accessRights = accessRights;
    }
}
