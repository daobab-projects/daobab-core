package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public interface BitField<T> {

    void writeValue(ByteBuffer byteBuffer, Integer position, T value);

    T readValue(ByteBuffer byteBuffer, Integer position);

    Class<T> getClazz();

    int calculateSpace(TableColumn column);


}
