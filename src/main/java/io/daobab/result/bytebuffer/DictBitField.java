package io.daobab.result.bytebuffer;

public enum DictBitField {

    BT_STRING(new BitFieldString()),
    BT_SHORT(new BitFieldShort()),
    BT_LONG(new BitFieldLong()),
    BT_INTEGER(new BitFieldInteger()),
    BT_DOUBLE(new BitFieldDouble()),
    BT_CHAR(new BitFieldChar()),
    BT_BOOLEAN(new BitFieldBoolean()),
    BT_BYTE(new BitFieldByte()),
    BT_FLOAT(new BitFieldFloat()),
    BT_DATE(new BitFieldDate()),
    BT_SQLDATE(new BitFieldSqlDate()),
    BT_TIMESTAMP(new BitFieldTimestamp()),
    BT_BIGDECIMAL(new BitFieldBigDecimal()),
    BT_BIGINTEGER(new BitFieldBigInteger());

    private final BitField field;

    DictBitField(BitField field) {
        this.field = field;
    }

    public BitField getField() {
        return field;
    }


}
