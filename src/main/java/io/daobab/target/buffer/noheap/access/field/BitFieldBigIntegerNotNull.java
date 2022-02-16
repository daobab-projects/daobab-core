package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldBigIntegerNotNull extends BitFieldComparable<BigInteger> {

    public BitFieldBigIntegerNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigInteger val) {
        byteBuffer.put(val.toByteArray());
    }

    @Override
    public BigInteger readValue(ByteBuffer byteBuffer, Integer position) {

        byte[] read = new byte[BitSize.BIG_INTEGER];
        byteBuffer.position(position);
        byteBuffer.get(read);
        return new BigInteger(read);
    }

    @Override
    public Class<BigInteger> getClazz() {
        return BigInteger.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.BIG_INTEGER;
    }

    @Override
    public Comparator<? super BigInteger> comparator() {
        return (Comparator<BigInteger>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }
}
