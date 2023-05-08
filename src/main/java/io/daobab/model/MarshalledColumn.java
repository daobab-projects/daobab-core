package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MarshalledColumn {

    private String entityClass;
    private String columnClass;

    public MarshalledColumn() {
    }

    public MarshalledColumn(Column<?, ?, ?> col) {
        setEntityClass(col.getEntityClass().getSimpleName());
        setColumnClass(col.getFieldClass().getSimpleName());
    }


    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getFieldClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }


}
