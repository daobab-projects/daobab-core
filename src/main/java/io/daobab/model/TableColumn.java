package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TableColumn {

    private final Column column;

    private int size;
    private int precision;

    private int decimalDigits = 0;
    private boolean nullable;
    private boolean lob;
    private boolean unique;
    private boolean primaryKey;
    private Object defaultValue;

    public TableColumn(Column<?, ?, ?> column) {
        this.column = column;
        this.size = 0;
        this.precision = 0;
    }


    public Column getColumn() {
        return column;
    }

    public int getSize() {
        return size;
    }

    public int getPrecision() {
        return precision;
    }


    public boolean isUnique() {
        return unique;
    }

    public TableColumn unique() {
        this.unique = true;
        return this;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public TableColumn primaryKey() {
        this.primaryKey = true;
        notNull();
        unique();
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public TableColumn notNull() {
        this.nullable = true;
        return this;
    }

    public boolean isLob() {
        return lob;
    }

    public TableColumn lob() {
        this.lob = true;
        return this;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public TableColumn defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TableColumn size(int size) {
        this.size = size;
        return this;
    }


    public TableColumn precision(int precision) {
        this.precision = precision;
        return this;
    }


    public int getScale() {
        return decimalDigits;
    }

    public TableColumn scale(int decimalDigits) {
        this.decimalDigits = decimalDigits;
        return this;
    }


}
