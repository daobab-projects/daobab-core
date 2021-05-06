package io.daobab.result;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class FieldsBuffer<F> extends LinkedList<F> implements FieldsProvider<F> {

    public FieldsBuffer() {
    }

    public FieldsBuffer(Collection<F> e) {
        addAll(e);
    }

    @Override
    public void forEach(Consumer<? super F> action) {
        FieldsProvider.super.forEach(action);
    }

    @Override
    public List<F> findMany() {
        return this;
    }

    @Override
    public Optional<F> findFirst() {
        return (isEmpty()) ? Optional.empty() : Optional.of(get(0));
    }

    @Override
    public long countAny() {
        return size();
    }


}
