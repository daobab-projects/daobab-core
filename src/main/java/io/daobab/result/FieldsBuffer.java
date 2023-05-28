package io.daobab.result;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class FieldsBuffer<F> extends LinkedList<F> implements FieldsProvider<F> {

    public FieldsBuffer() {
    }

    public FieldsBuffer(Collection<F> e) {
        addAll(e);
    }

    @Override
    public List<F> findMany() {
        return this;
    }

    @Override
    public Optional<F> findFirst() {
        if (isEmpty()){
            return Optional.empty();
        }
        return Optional.of(get(0));
    }


}
