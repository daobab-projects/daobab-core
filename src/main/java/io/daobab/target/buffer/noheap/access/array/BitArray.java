package io.daobab.target.buffer.noheap.access.array;

import io.daobab.target.buffer.noheap.access.field.BitField;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Comparator;

public interface BitArray<T, B extends BitField<T>> extends BitField<T[]> {

    void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list);

    void writeValue(ByteBuffer byteBuffer, Integer position, T[] array);

    T[] readValue(ByteBuffer byteBuffer, Integer position);

    T[] createArrayForLength(int length);

    B getTypeBitField();

    int getTypeSize();

    int calculateSpace(int length);

    Comparator<? super T> comparator();
}
