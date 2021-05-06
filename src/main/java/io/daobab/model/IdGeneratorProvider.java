package io.daobab.model;

public interface IdGeneratorProvider<F> {

    F generateId();
}
