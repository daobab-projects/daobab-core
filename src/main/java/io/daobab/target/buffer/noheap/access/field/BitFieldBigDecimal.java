package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

public class BitFieldBigDecimal implements BitField<BigDecimal> {

    public BitFieldBigDecimal(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigDecimal bval) {
        if (bval != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.CHECK_NULL, bval.precision());
            byteBuffer.putInt(position + BitSize.CHECK_NULL + BitSize.INT, bval.scale());
            byte[] arr = bval.unscaledValue().toByteArray();
            byteBuffer.putInt(position + BitSize.CHECK_NULL + BitSize.INT + BitSize.INT, arr.length);
            byteBuffer.position(position + BitSize.CHECK_NULL + BitSize.INT + BitSize.INT + BitSize.INT);
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

        int precision = byteBuffer.getInt(position + BitSize.CHECK_NULL);
        int scale = byteBuffer.getInt(position + BitSize.CHECK_NULL + BitSize.INT);


        int length = byteBuffer.getInt(position + BitSize.CHECK_NULL + BitSize.INT + BitSize.INT);
        byte[] read = new byte[length];
        byteBuffer.position(position + BitSize.CHECK_NULL + BitSize.INT + BitSize.INT + BitSize.INT);
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
        return BitSize.BIG_DECIMAL + BitSize.CHECK_NULL;
    }

}
