package io.daobab.result;

import io.daobab.model.Column;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
class ColumnAndPointer {

    private final Column column;
    private final int pointer;

    public ColumnAndPointer(Column column, int pointer) {
        this.column = column;
        this.pointer = pointer;
    }

    public int getPointer() {
        return pointer;
    }

    public Column getColumn() {
        return column;
    }
}
