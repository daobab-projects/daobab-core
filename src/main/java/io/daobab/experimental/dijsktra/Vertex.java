package io.daobab.experimental.dijsktra;

import com.sun.istack.internal.NotNull;
import io.daobab.converter.duplicator.duplication.EntityDuplication;
import io.daobab.model.Entity;

public class Vertex {
    private final String id;
    private final String name;
    private final Entity entity;

    public Vertex(@NotNull Entity entity) {
        this.entity = entity;
        String entityName = EntityDuplication.getEntityName(entity, null);
        this.id = entityName;
        this.name = entityName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }
}
