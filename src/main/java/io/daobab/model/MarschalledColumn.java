package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MarschalledColumn {

    private String entityClass;
    private String columnClass;

    public MarschalledColumn() {
    }

    public MarschalledColumn(Column<?, ?, ?> col) {
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
