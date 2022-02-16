package io.daobab.target.buffer.noheap.access.array;

import io.daobab.target.buffer.noheap.access.field.BitField;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;

public interface BitArray<T, B extends BitField<T>> extends BitField<T[]> {

    void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list);

    void writeValue(ByteBuffer byteBuffer, Integer position, T[] array);

    void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, Collection<T> list);

    void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, T[] array);

    T[] readValue(ByteBuffer byteBuffer, Integer position);

    T[] readValueWithLength(ByteBuffer byteBuffer, Integer position, int length);

    List<T> readValueListWithLength(ByteBuffer byteBuffer, Integer position, int length);

    T[] createArrayForLength(int length);

    B getTypeBitField();

    int getTypeSize();

    int calculateSpace(int length);

}
