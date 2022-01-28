package io.daobab.target.buffer.noheap.index;


import io.daobab.statement.condition.Operator;


public interface BitBufferIndexBase<F> {

    Integer[] filter(Operator operator, F... keys);

    Integer[] filter(Operator operator, Object key);

    Integer[] filterNegative(Operator operator, Object key1);


    void addValue(F value, int pointer);

    boolean removeValue(F value, int pointer);

}
