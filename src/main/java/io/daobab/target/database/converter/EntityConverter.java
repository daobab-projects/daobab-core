package io.daobab.target.database.converter;

import java.util.function.Consumer;

public interface EntityConverter<F, E> {

    void addKey(F key, Consumer<E> consumer);

    void readEntities();

    void applyEntities();

}
