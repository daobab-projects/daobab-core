package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldBigDecimalNotNull extends BitFieldComparable<BigDecimal> {

    public BitFieldBigDecimalNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, BigDecimal bval) {

        byteBuffer.put(position, (byte) bval.precision());
//        byteBuffer.putInt(position, bval.precision());
        position += BitSize.BYTE;
        byteBuffer.put(position, (byte) bval.scale());
        position += BitSize.BYTE;
        byte[] arr = bval.unscaledValue().toByteArray();
        byteBuffer.put(position, (byte) arr.length);
        position += BitSize.BYTE;
        byteBuffer.position(position);
        byteBuffer.put(bval.unscaledValue().toByteArray());

    }

    @Override
    public BigDecimal readValue(ByteBuffer byteBuffer, Integer position) {

        int precision = byteBuffer.get(position);
        position += BitSize.BYTE;
        int scale = byteBuffer.get(position);
        position += BitSize.BYTE;

        int length = byteBuffer.get(position);
        position += BitSize.BYTE;
        byte[] read = new byte[length];
        byteBuffer.position(position);
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
        return BitSize.calculateBigDecimalSize(column.getSize(), false);
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
