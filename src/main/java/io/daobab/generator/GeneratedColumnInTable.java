package io.daobab.generator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
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

    public int getColumnSize() {
        return columnSize;
    }

    public GeneratedColumnInTable setColumnSize(int columnSize) {
        this.columnSize = columnSize;
        return this;
    }

    public String getDecimalDigits() {
        return decimalDigits;
    }

    public GeneratedColumnInTable setDecimalDigits(String decimalDigits) {
        this.decimalDigits = decimalDigits;
        return this;
    }

    public String getNullable() {
        return nullable;
    }

    public GeneratedColumnInTable setNullable(String nullable) {
        this.nullable = nullable;
        return this;
    }

    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public GeneratedColumnInTable setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public GeneratedColumnInTable setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public boolean isPk() {
        return pk;
    }

    public GeneratedColumnInTable setPk(boolean pk) {
        this.pk = pk;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public GeneratedColumnInTable setPosition(int position) {
        this.position = position;
        return this;
    }

    public int getDataType() {
        return dataType;
    }

    public GeneratedColumnInTable setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }
}
