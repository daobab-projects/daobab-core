package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

public class BitSize {
    public static final int NULL = 1;
    public static final int SHORT = 2;
    public static final int INT = 4;
    public static final int BYTE = 1;
    public static final int UNSIGNED_INT = 4;
    public static final int UNSIGNED_SHORT = 2;
    public static final int UNSIGNED_BYTE = 1;
    public static final int LONG = 8;
    public static final int FLOAT = 4;
    public static final int DOUBLE = 8;
    public static final int TIMESTAMP_SQL = LONG + INT;
    public static final int DATE_SQL = LONG + INT;
    public static final int DATE_UTIL = LONG + INT;
    public static final int BIG_INTEGER = 64;
//    public static final int BIG_DECIMAL = BIG_INTEGER + INT + INT;

    public static int calculateStringSize(int length, boolean useNullCheck) {
        return length * 6 + BitSize.INT + (useNullCheck ? 0 : BitSize.NULL);
    }

    public static int calculateBigDecimalSize(int length, boolean useNullCheck) {
        return length * 6 + BitSize.INT + (useNullCheck ? 0 : BitSize.NULL);
    }

    public static int calculateBigDecimalSize2(int length, boolean useNullCheck) {
        return length * 6 + BitSize.INT + (useNullCheck ? 0 : BitSize.NULL);
    }

    public static int calculateStringSize(TableColumn tableColumn, boolean useNullCheck) {
        return tableColumn.getSize() * 6 + BitSize.INT + (useNullCheck ? 0 : BitSize.NULL);
    }
}
