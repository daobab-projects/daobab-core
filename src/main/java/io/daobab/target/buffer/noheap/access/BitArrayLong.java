package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitArrayLong extends BitField<Long[]> {

    private final BitFieldLong bitFieldLong = new BitFieldLong();

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Long[] array) {

        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, array.length);

            int counter = 0;

            for (Long aLong : array) {
                bitFieldLong.writeValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter, aLong);
                counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
            }

            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public Long[] readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return new Long[]{};
        }
        int length = byteBuffer.getInt(position + CHECK_NULL_SIZE);
        Long[] rv = new Long[length];

        int counter = 0;
        for (int i = 0; i < length; i++) {
            rv[i] = bitFieldLong.readValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter);
            counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
        }
        return rv;
    }

    @Override
    public Class<Long[]> getClazz() {
        return Long[].class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return INT_SIZE + CHECK_NULL_SIZE;
    }


}
