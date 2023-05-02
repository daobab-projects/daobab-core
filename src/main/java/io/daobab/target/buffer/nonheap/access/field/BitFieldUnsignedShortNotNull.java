package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldUnsignedShortNotNull extends BitFieldComparable<Short> {

    public BitFieldUnsignedShortNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldUnsignedShortNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Short val) {
        byteBuffer.position(position);
        byteBuffer.put(new byte[] {
                (byte)((val >> 8) & 0xff),
                (byte)(val & 0xff)});
    }

    @Override
    public Short readValue(ByteBuffer byteBuffer, Integer position) {
        byteBuffer.position(position);
        byte[] read = new byte[BitSize.UNSIGNED_SHORT];
        byteBuffer.get(read);
        return (short)((read[1] & 0xFF) |
                ((read[0] & 0xFF) <<  8));

    }

    @Override
    public Class<Short> getClazz() {
        return Short.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.UNSIGNED_SHORT;
    }


    @Override
    public Comparator<? super Short> comparator() {
        return (Comparator<Short>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
