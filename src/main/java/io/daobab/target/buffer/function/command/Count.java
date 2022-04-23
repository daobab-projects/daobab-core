package io.daobab.target.buffer.function.command;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Count extends BufferFunction<Object> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Plates apply(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Long count = plates.stream().map(p -> p.getValue(function.getFinalColumn())).filter(Objects::nonNull).count();
        Plates rv = getClonedPlates(plates, true);
        rv.get(0).setValue(function, count);
        return rv;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        return Collections.singletonList(plates.stream().filter(Objects::nonNull).count());
    }

    @Override
    public FunctionType getType() {
        return FunctionType.AGGREGATED;
    }

}
