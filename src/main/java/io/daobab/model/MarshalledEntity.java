package io.daobab.model;

/**
 * The transport form of an entity reference: the fully qualified name of its class. Used when (de)serializing
 * queries for remote execution.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MarshalledEntity {

    private String entityClass;

    public MarshalledEntity() {
    }

    /**
     * @param col a column of the entity to marshal
     */
    public MarshalledEntity(Column<?, ?, ?> col) {
        setEntityClass(col.entityClass().getName());
    }

    /**
     * The entity's fully qualified class name.
     */
    public String getEntityClass() {
        return entityClass;
    }

    /** Sets the entity's class name. */
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }


}
