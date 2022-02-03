package io.daobab.result;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
    public List<F> findMany() {
        return this;
    }



}
