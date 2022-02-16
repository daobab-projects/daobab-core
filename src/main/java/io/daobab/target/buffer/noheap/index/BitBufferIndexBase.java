package io.daobab.target.buffer.noheap.index;


import io.daobab.statement.condition.Operator;

import java.util.Collection;

public interface BitBufferIndexBase<F> {

    Collection<Integer> filter(Operator operator, F... keys);

    Collection<Integer> filter(Operator operator, Object key);

    Collection<Integer> filterNegative(Operator operator, Object key1);


    void addValue(F value, int pointer);

    boolean removeValue(F value, int pointer);

}
