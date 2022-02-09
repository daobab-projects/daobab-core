package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BitFieldBigIntegerNotNull implements BitField<BigInteger> {

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
}
