package io.daobab.target.buffer.noheap.access.array;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitField;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.nio.ByteBuffer;
import java.util.Collection;

public abstract class BitArrayBase<T, B extends BitField<T>> implements BitArray<T, B> {

    public void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list) {
        writeValue(byteBuffer, position, list.toArray(createArrayForLength(list.size())));
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, T[] array) {
        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            position = position + BitSize.CHECK_NULL;
            byteBuffer.putInt(position, array.length);
            position = position + BitSize.INT;

            for (T integer : array) {
                getTypeBitField().writeValue(byteBuffer, position, integer);
                position = position + BitSize.INT;
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
        position = position + BitSize.CHECK_NULL;
        int length = byteBuffer.getInt(position);
        position = position + BitSize.INT;
        T[] rv = createArrayForLength(length);

        for (int i = 0; i < length; i++) {
            rv[i] = getTypeBitField().readValue(byteBuffer, position);
            position = position + BitSize.INT;
        }
        return rv;
    }

    @Deprecated
    @Override
    public int calculateSpace(TableColumn column) {
        throw new DaobabException("Bit array does not handle this logic");
    }

    public int calculateSpace(int length) {
        return BitSize.CHECK_NULL + BitSize.INT + (length * getTypeSize());
    }
}
