package io.daobab.target.buffer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface BufferIdGeneratorSupplier<F> {

    F generateId(BufferQueryTarget currentTarget);
}
