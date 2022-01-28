package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.function.Function;

public interface BitField<T> {

    void writeValue(ByteBuffer byteBuffer, Integer position, T value);

    T readValue(ByteBuffer byteBuffer, Integer position);

    Class<T> getClazz();

    int calculateSpace(TableColumn column);

    Comparator<? super T> comparator();

    Function<T, WherePredicate<T>> getPredicate(Operator operator);


}
