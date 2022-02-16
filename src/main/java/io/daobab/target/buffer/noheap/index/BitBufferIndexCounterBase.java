package io.daobab.target.buffer.noheap.index;


import io.daobab.statement.condition.Operator;

public interface BitBufferIndexCounterBase<F> {

    Integer countFilter(Operator operator, F... keys);

    Integer countFilter(Operator operator, Object key);

    Integer countFilterNegative(Operator operator, Object key1);


}
