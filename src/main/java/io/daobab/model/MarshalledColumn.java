package io.daobab.model;

/**
 * The transport form of a {@link Column}: the (simple) names of its entity class and its field type. Used when
 * (de)serializing queries for remote execution.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MarshalledColumn {

    private String entityClass;
    private String columnClass;

    public MarshalledColumn() {
    }

    /**
     * @param col the column to marshal
     */
    public MarshalledColumn(Column<?, ?, ?> col) {
        setEntityClass(col.entityClass().getSimpleName());
        setColumnClass(col.getFieldClass().getSimpleName());
    }


    /**
     * The column's entity class (simple) name.
     */
    public String getEntityClass() {
        return entityClass;
    }

    /** Sets the column's entity class name. */
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    /** The column's field type (simple) name. */
    public String getFieldClass() {
        return columnClass;
    }

    /** Sets the column's field type name. */
    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }


}
