package io.daobab.target.buffer.nonheap.index;


import io.daobab.statement.condition.Operator;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface BitBufferIndexBase<F> {

    Integer[] filter(Operator operator, F... keys);

    Integer[] filter(Operator operator, Object key);

    Integer[] filterNegative(Operator operator, Object key1);


    void addValue(F value, int pointer);

    boolean removeValue(F value, int pointer);

}
