package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MarshalledEntity {

    private String entityClass;

    public MarshalledEntity() {
    }

    public MarshalledEntity(Column<?, ?, ?> col) {
        setEntityClass(col.getEntityClass().getName());
    }


    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }


}
