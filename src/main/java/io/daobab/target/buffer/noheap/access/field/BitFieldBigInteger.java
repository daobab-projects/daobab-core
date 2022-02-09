package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BitFieldBigInteger implements BitField<BigInteger> {

    public BitFieldBigInteger(TableColumn tableColumn) {
    }

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
        byte[] read = new byte[BitSize.BIG_INTEGER];
        byteBuffer.position(position + BitSize.CHECK_NULL);
        byteBuffer.get(read);
        return new BigInteger(read);
    }

    @Override
    public Class<BigInteger> getClazz() {
        return BigInteger.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.BIG_INTEGER + BitSize.CHECK_NULL;
    }
}
