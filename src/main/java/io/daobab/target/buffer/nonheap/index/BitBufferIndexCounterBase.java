package io.daobab.target.buffer.nonheap.index;


import io.daobab.statement.condition.Operator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface BitBufferIndexCounterBase<F> {

    Integer countFilter(Operator operator, Object key);

    Integer countFilterNegative(Operator operator, Object key1);


}
