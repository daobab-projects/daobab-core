package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldBigDecimal extends BitFieldComparable<BigDecimal> {

    public BitFieldBigDecimal(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigDecimal bval) {
        if (bval != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.NULL, bval.precision());
            byteBuffer.putInt(position + BitSize.NULL + BitSize.INT, bval.scale());
            byte[] arr = bval.unscaledValue().toByteArray();
            byteBuffer.putInt(position + BitSize.NULL + BitSize.INT + BitSize.INT, arr.length);
            byteBuffer.position(position + BitSize.NULL + BitSize.INT + BitSize.INT + BitSize.INT);
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

        int precision = byteBuffer.getInt(position + BitSize.NULL);
        int scale = byteBuffer.getInt(position + BitSize.NULL + BitSize.INT);


        int length = byteBuffer.getInt(position + BitSize.NULL + BitSize.INT + BitSize.INT);
        byte[] read = new byte[length];
        byteBuffer.position(position + BitSize.NULL + BitSize.INT + BitSize.INT + BitSize.INT);
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
        if (column.getSize() == 0) {
            throw new DaobabException("Field " + column.getColumn().toString() + " needs size specified.");
        }
        return BitSize.calculateBigDecimalSize(column.getSize(), true);
    }


    @Override
    public Comparator<? super BigDecimal> comparator() {
        return (Comparator<BigDecimal>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }


}
