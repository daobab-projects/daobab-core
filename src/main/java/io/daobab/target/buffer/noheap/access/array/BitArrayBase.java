package io.daobab.target.buffer.noheap.access.array;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.access.field.BitField;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public abstract class BitArrayBase<T, B extends BitField<T>> implements BitArray<T, B> {


    @Override
    public void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, Collection<T> list) {
        writeValue(byteBuffer, position, list.toArray(createArrayForLength(list.size())));
    }

    @Override
    public void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, T[] array) {
        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            position += BitSize.NULL;

            for (T integer : array) {
                getTypeBitField().writeValue(byteBuffer, position, integer);
                position += BitSize.INT;
            }
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }


    public void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list) {
        writeValue(byteBuffer, position, list.toArray(createArrayForLength(list.size())));
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, T[] array) {
        if (array != null) {
            byteBuffer.put(position, (byte) 1);
            position += BitSize.NULL;
            byteBuffer.putInt(position, array.length);
            position += BitSize.INT;

            for (T integer : array) {
                getTypeBitField().writeValue(byteBuffer, position, integer);
                position += BitSize.INT;
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
        position += BitSize.NULL;
        int length = byteBuffer.getInt(position);
        position += BitSize.INT;
        T[] rv = createArrayForLength(length);

        for (int i = 0; i < length; i++) {
            rv[i] = getTypeBitField().readValue(byteBuffer, position);
            position += BitSize.INT;
        }
        return rv;
    }

    @Override
    public T[] readValueWithLength(ByteBuffer byteBuffer, Integer position, int length) {
        if (length == 0) {
            return createArrayForLength(0);
        }
        T[] rv = createArrayForLength(length);

        for (int i = 0; i < length; i++) {
            rv[i] = getTypeBitField().readValue(byteBuffer, position);
            position += BitSize.INT;
        }
        return rv;
    }


    @Override
    public List<T> readValueListWithLength(ByteBuffer byteBuffer, Integer position, int length) {
        //todo: check nulls
        position += BitSize.NULL;
        List<T> rv = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            rv.add(getTypeBitField().readValue(byteBuffer, position));
            position += BitSize.INT;
        }
        return rv;
    }

    @Deprecated
    @Override
    public int calculateSpace(TableColumn column) {
        throw new DaobabException("Bit array does not handle this logic");
    }

    public int calculateSpace(int length) {
        return BitSize.NULL + BitSize.INT + (length * getTypeSize());
    }

    @Override
    public Comparator<? super T[]> comparator() {
        return null;
    }

    @Override
    public Function<T[], WherePredicate<T[]>> getPredicate(Operator operator) {
        return null;
    }
}
