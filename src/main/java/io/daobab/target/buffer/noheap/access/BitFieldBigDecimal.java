package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

public class BitFieldBigDecimal extends BitField<BigDecimal> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigDecimal bval) {
        if (bval != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, bval.precision());
            byteBuffer.putInt(position + CHECK_NULL_SIZE + INT_SIZE, bval.scale());
            byte[] arr = bval.unscaledValue().toByteArray();
            byteBuffer.putInt(position + CHECK_NULL_SIZE + INT_SIZE + INT_SIZE, arr.length);
            byteBuffer.position(position + CHECK_NULL_SIZE + INT_SIZE + INT_SIZE + INT_SIZE);
            byteBuffer.put(bval.unscaledValue().toByteArray());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public BigDecimal readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }

        int precision = byteBuffer.getInt(position + CHECK_NULL_SIZE);
        int scale = byteBuffer.getInt(position + CHECK_NULL_SIZE + INT_SIZE);


        int length = byteBuffer.getInt(position + CHECK_NULL_SIZE + INT_SIZE + INT_SIZE);
        byte[] read = new byte[length];
        byteBuffer.position(position + CHECK_NULL_SIZE + INT_SIZE + INT_SIZE + INT_SIZE);
        byteBuffer.get(read);
        BigDecimal rv = new BigDecimal(new BigInteger(read), scale);
        rv = rv.setScale(scale, RoundingMode.HALF_UP);
        return rv;
    }


    @Override
    public Class<BigDecimal> getClazz() {
        return BigDecimal.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BIG_INTEGER_SIZE + CHECK_NULL_SIZE + INT_SIZE + INT_SIZE;
    }

}
