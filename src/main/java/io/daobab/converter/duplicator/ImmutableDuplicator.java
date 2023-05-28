package io.daobab.converter.duplicator;


public class ImmutableDuplicator<T> extends Duplicator<T> {

    @Override
    public T duplicate(T obj) {
        return obj;
    }
}
