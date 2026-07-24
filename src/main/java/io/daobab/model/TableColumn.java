package io.daobab.model;

/**
 * A {@link Column} together with its table-level metadata: size, precision, scale, the default value and the
 * nullability / {@code LOB} / {@code UNIQUE} / {@code PRIMARY KEY} flags. Built fluently inside an entity's
 * {@code columns()} method, e.g. {@code new TableColumn(colTitle()).size(256).notNull()}.
 *
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

    /**
     * @param column the described column
     */
    public TableColumn(Column<?, ?, ?> column) {
        this.column = column;
        this.size = 0;
        this.precision = 0;
    }


    /**
     * The described column.
     */
    public Column getColumn() {
        return column;
    }

    /** The column size (length), or {@code 0} when unset. */
    public int getSize() {
        return size;
    }

    /** The column precision, or {@code 0} when unset. */
    public int getPrecision() {
        return precision;
    }


    /** Whether the column is unique. */
    public boolean isUnique() {
        return unique;
    }

    /** Marks the column unique. */
    public TableColumn unique() {
        this.unique = true;
        return this;
    }

    /** Whether the column is the primary key. */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /** Marks the column as the primary key (also not-null and unique). */
    public TableColumn primaryKey() {
        this.primaryKey = true;
        notNull();
        unique();
        return this;
    }

    /** Whether the column was flagged with {@link #notNull()}. */
    public boolean isNullable() {
        return nullable;
    }

    /** Marks the column as not-null. */
    public TableColumn notNull() {
        this.nullable = true;
        return this;
    }

    /** Whether the column holds large-object data. */
    public boolean isLob() {
        return lob;
    }

    /** Marks the column as a large object ({@code LOB}). */
    public TableColumn lob() {
        this.lob = true;
        return this;
    }

    /** The column default value, or {@code null}. */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /** Sets the column default value. */
    public TableColumn defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /** Sets the column size (length). */
    public TableColumn size(int size) {
        this.size = size;
        return this;
    }


    /** Sets the column precision. */
    public TableColumn precision(int precision) {
        this.precision = precision;
        return this;
    }


    /** The column scale (number of decimal digits). */
    public int getScale() {
        return decimalDigits;
    }

    /** Sets the column scale (number of decimal digits). */
    public TableColumn scale(int decimalDigits) {
        this.decimalDigits = decimalDigits;
        return this;
    }


}
