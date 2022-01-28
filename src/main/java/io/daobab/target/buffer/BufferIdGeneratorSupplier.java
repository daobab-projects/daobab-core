package io.daobab.target.buffer;

public interface BufferIdGeneratorSupplier<F> {

    F generateId(BufferQueryTarget currentTarget);
}
