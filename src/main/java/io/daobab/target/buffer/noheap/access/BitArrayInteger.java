package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitArrayInteger extends BitField<Integer[]> {

    private final BitFieldInteger bitFieldInteger = new BitFieldInteger();

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer[] array) {

        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, array.length);

            int counter = 0;

            for (Integer integer : array) {
                bitFieldInteger.writeValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter, integer);
                counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
            }

            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public Integer[] readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return new Integer[]{};
        }
        int length = byteBuffer.getInt(position + CHECK_NULL_SIZE);
        Integer[] rv = new Integer[length];

        int counter = 0;
        for (int i = 0; i < length; i++) {
            rv[i] = bitFieldInteger.readValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter);
            counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
        }
        return rv;
    }

    @Override
    public Class<Integer[]> getClazz() {
        return Integer[].class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return INT_SIZE + CHECK_NULL_SIZE;
    }


}
