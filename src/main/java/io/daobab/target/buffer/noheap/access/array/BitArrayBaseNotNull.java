package io.daobab.target.buffer.noheap.access.array;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitField;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BitArrayBaseNotNull<T, B extends BitField<T>> implements BitArray<T, B> {

    public void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list) {
        writeValue(byteBuffer, position, list.toArray(createArrayForLength(list.size())));
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, T[] array) {
        byteBuffer.putInt(position, array.length);
        position = position + BitSize.INT;

        for (T integer : array) {
            getTypeBitField().writeValue(byteBuffer, position, integer);
            position = position + getTypeSize();
        }

    }

    @Override
    public T[] readValue(ByteBuffer byteBuffer, Integer position) {
        int length = byteBuffer.getInt(position);
        T[] rv = createArrayForLength(length);
        position = position + BitSize.INT;

        for (int i = 0; i < length; i++) {
            rv[i] = getTypeBitField().readValue(byteBuffer, position);
            position = position + getTypeSize();
        }
        return rv;
    }

    public List<T> subListKey(ByteBuffer byteBuffer, Integer position, Integer fromKeyNo, Integer toKeyNo) {
        int length = byteBuffer.getInt(position);

        if (toKeyNo > length) {
            toKeyNo = length;
        }

        int subLength = toKeyNo - fromKeyNo;
        List<T> rv = new ArrayList<>(subLength);

        position = position + BitSize.INT + getTypeSize() * fromKeyNo;

        for (int i = fromKeyNo; i < toKeyNo + 1; i++) {
            rv.add(getTypeBitField().readValue(byteBuffer, position));
            position = position + getTypeSize();
        }
        return rv;
    }

    @Deprecated
    @Override
    public int calculateSpace(TableColumn column) {
        throw new DaobabException("Bit array does not handle this logic");
    }

    public int calculateSpace(int length) {
        return BitSize.INT + (length * getTypeSize());
    }
}
