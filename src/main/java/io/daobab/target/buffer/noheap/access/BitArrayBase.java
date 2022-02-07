package io.daobab.target.buffer.noheap.access;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public abstract class BitArrayBase<T, B extends BitField<T>> extends BitField<T[]> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, T[] array) {

        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, array.length);

            int counter = 0;

            for (T integer : array) {
                getTypeBitField().writeValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter, integer);
                counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
            }

            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public T[] readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return createArrayForLength(0);
        }
        int length = byteBuffer.getInt(position + CHECK_NULL_SIZE);
        T[] rv = createArrayForLength(length);

        int counter = 0;
        for (int i = 0; i < length; i++) {
            rv[i] = getTypeBitField().readValue(byteBuffer, position + CHECK_NULL_SIZE + INT_SIZE + counter);
            counter = counter + BitFieldInteger.BIG_INTEGER_SIZE;
        }
        return rv;
    }

    protected abstract T[] createArrayForLength(int length);

    protected abstract B getTypeBitField();

    protected abstract int getTypeSize();

    @Deprecated
    @Override
    public int calculateSpace(TableColumn column) {
        throw new DaobabException("Bit array does not handle this logic");
    }

    public int calculateSpace(int length) {
        return CHECK_NULL_SIZE + INT_SIZE + length * getTypeSize();
    }
}
