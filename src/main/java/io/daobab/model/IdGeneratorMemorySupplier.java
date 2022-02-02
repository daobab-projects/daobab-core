package io.daobab.model;

import io.daobab.target.buffer.BufferQueryTarget;

public interface IdGeneratorMemorySupplier<F> {

    F generateId(BufferQueryTarget currentTarget);
}
