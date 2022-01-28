package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldUnsignedInteger extends BitFieldComparable<Integer> {

    private final BitFieldUnsignedByteNotNull byteField;

    public BitFieldUnsignedInteger() {
        this(new EmptyTableColumn());
    }

    public BitFieldUnsignedInteger(TableColumn tableColumn) {
        byteField = new BitFieldUnsignedByteNotNull();
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        if (val==null){
            byteField.writeNullInformation(byteBuffer,position);
            return;
        }
        byteField.writeNotNullInformation(byteBuffer,position);
        byteBuffer.position(position+BitSize.UNSIGNED_BYTE);
        byteBuffer.put(new byte[] {
                (byte)((val >> 24) & 0xff),
                (byte)((val >> 16) & 0xff),
                (byte)((val >> 8) & 0xff),
                (byte)(val & 0xff)});
    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteField.isNull(byteBuffer,position)){
            return null;
        }
        byteBuffer.position(position+BitSize.UNSIGNED_BYTE);
        byte[] read = new byte[BitSize.UNSIGNED_INT];
        byteBuffer.get(read);
        return ((read[3] & 0xFF) |
                ((read[2] & 0xFF) <<  8) |
                ((read[1] & 0xFF) << 16) |
                ((read[0] & 0xFF) << 24));
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.UNSIGNED_INT;
    }


    @Override
    public Comparator<? super Integer> comparator() {
        return (Comparator<Integer>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
