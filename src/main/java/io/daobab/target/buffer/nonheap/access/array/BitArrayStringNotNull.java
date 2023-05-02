package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldStringNotNull;

public class BitArrayStringNotNull extends BitArrayBaseNotNull<String, BitFieldStringNotNull> {

    private final BitFieldStringNotNull instance;
    private final int typeSize;

    public BitArrayStringNotNull(TableColumn tableColumn) {
        instance = new BitFieldStringNotNull(tableColumn);
        this.typeSize = tableColumn.getSize();
    }
//    /**
//     * @param length - String max length
//     */
//    public BitArrayStringNotNull(int length) {
//        this.typeSize = BitFieldStringNotNull.calculateSpaceForLength(length);
//        instance = new BitFieldStringNotNull(length);
//    }

    @Override
    public String[] createArrayForLength(int length) {
        return new String[length];
    }

    @Override
    public BitFieldStringNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return typeSize;
    }

    @Override
    public Class<String[]> getClazz() {
        return String[].class;
    }

}
