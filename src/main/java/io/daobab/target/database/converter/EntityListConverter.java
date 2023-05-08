package io.daobab.target.database.converter;

import java.util.List;
import java.util.function.Consumer;

public interface EntityListConverter<F, E> {

    void addKey(F key, Consumer<List<E>> consumer);

    void readEntities(List<E> entities);

    void applyEntities();

}
