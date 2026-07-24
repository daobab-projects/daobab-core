package io.daobab.generator;

/**
 * The per-table metadata of a generated column read from the JDBC {@code DatabaseMetaData}: size, decimal
 * digits, nullability, auto-increment flag, remarks, ordinal position, JDBC data type and whether it is a
 * primary key. All setters are fluent. The same column can occur in several tables with different metadata,
 * which is why this is kept per table.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class GeneratedColumnInTable {

    private int columnSize;
    private String decimalDigits;
    private String nullable;
    private String isAutoIncrement;
    private String remarks;
    private int position;
    private int dataType;
    private boolean pk;

    /**
     * The column size (length).
     */
    public int getColumnSize() {
        return columnSize;
    }

    /** Sets the column size. */
    public GeneratedColumnInTable setColumnSize(int columnSize) {
        this.columnSize = columnSize;
        return this;
    }

    /** The number of decimal digits (scale), as reported by JDBC. */
    public String getDecimalDigits() {
        return decimalDigits;
    }

    /** Sets the number of decimal digits. */
    public GeneratedColumnInTable setDecimalDigits(String decimalDigits) {
        this.decimalDigits = decimalDigits;
        return this;
    }

    /** The raw JDBC nullable flag ({@code "1"} when nullable). */
    public String getNullable() {
        return nullable;
    }

    /** The raw JDBC auto-increment flag. */
    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    /** Sets the raw JDBC auto-increment flag. */
    public GeneratedColumnInTable setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
        return this;
    }

    /** The column remarks/comment. */
    public String getRemarks() {
        return remarks;
    }

    /** Sets the column remarks/comment. */
    public GeneratedColumnInTable setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /** Whether the column is part of the primary key. */
    public boolean isPk() {
        return pk;
    }

    /** Sets whether the column is part of the primary key. */
    public GeneratedColumnInTable setPk(boolean pk) {
        this.pk = pk;
        return this;
    }

    /** The 1-based ordinal position of the column in the table. */
    public int getPosition() {
        return position;
    }

    /** Sets the ordinal position. */
    public GeneratedColumnInTable setPosition(int position) {
        this.position = position;
        return this;
    }

    /** The JDBC data type ({@link java.sql.Types}). */
    public int getDataType() {
        return dataType;
    }

    /** Sets the JDBC data type. */
    public GeneratedColumnInTable setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }

    /** Whether the JDBC nullable flag equals {@code "1"}. */
    public boolean isNullable() {
        return "1".equals(getNullable());
    }

    /** Sets the raw JDBC nullable flag. */
    public GeneratedColumnInTable setNullable(String nullable) {
        this.nullable = nullable;
        return this;
    }
}
