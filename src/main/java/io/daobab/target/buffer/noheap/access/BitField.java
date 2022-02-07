package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public abstract class BitField<T> {

    protected static int CHECK_NULL_SIZE = 1;
    protected static int BIG_INTEGER_SIZE = 64;
    protected static int INT_SIZE = 4;
    protected static int SHORT_SIZE = 2;
    protected static int FLOAT_SIZE = 4;
    protected static int LONG_SIZE = 8;
    protected static int DOUBLE_SIZE = 8;

    public abstract void writeValue(ByteBuffer byteBuffer, Integer position, T value);

    public abstract T readValue(ByteBuffer byteBuffer, Integer position);

    public abstract Class<T> getClazz();

    public abstract int calculateSpace(TableColumn column);

}
