package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BitFieldBigInteger extends BitField<BigInteger> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigInteger val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.position(position + 1);
            byteBuffer.put(val.toByteArray());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public BigInteger readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        byte[] read = new byte[BIG_INTEGER_SIZE];
        byteBuffer.position(position + CHECK_NULL_SIZE);
        byteBuffer.get(read);
        return new BigInteger(read);
    }

    @Override
    public Class<BigInteger> getClazz() {
        return BigInteger.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BIG_INTEGER_SIZE + CHECK_NULL_SIZE;
    }
}
