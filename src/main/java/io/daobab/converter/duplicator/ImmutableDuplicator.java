package io.daobab.converter.duplicator;

import java.time.LocalDate;

public class ImmutableDuplicator<T> extends Duplicator<T> {

    @Override
    public T duplicate(T obj) {
        return obj;
    }
}
