package io.daobab.target.buffer.nonheap.access.array;

import io.daobab.target.buffer.nonheap.access.field.BitField;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface BitArray<T, B extends BitField<T>> extends BitField<T[]> {

    void writeValue(ByteBuffer byteBuffer, Integer position, Collection<T> list);

    void writeValue(ByteBuffer byteBuffer, Integer position, T[] array);

    void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, Collection<T> list);

    void writeValueWithoutLength(ByteBuffer byteBuffer, Integer position, T[] array);

    T[] readValue(ByteBuffer byteBuffer, Integer position);

    T[] readValueWithLength(ByteBuffer byteBuffer, Integer position, int length);

    void readValueListWithLength(ByteBuffer byteBuffer, T[] readTo, Integer position, int length);

    T[] createArrayForLength(int length);

    B getTypeBitField();

    int getTypeSize();

    int calculateSpace(int length);

}
