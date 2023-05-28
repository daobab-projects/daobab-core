package io.daobab.target.buffer.single.function.aggregate;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.function.AggregateFunction;

public class FunctionCount extends AggregateFunction {


    @Override
    public String mode() {
        return "COUNT";
    }

    @Override
    public <F> String applyFunction(Entities entities, ColumnFunction<?, F, ?, ?> columnFunction) {
        return String.valueOf(entities.size());
    }
}
