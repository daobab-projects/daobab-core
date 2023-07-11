package io.daobab.target.buffer.single.function;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Entities;

public abstract class AggregateFunction {

    public abstract String mode();

    public abstract <F> String applyFunction(Entities entities, ColumnFunction<?, F, ?, ?> columnFunction);
}
