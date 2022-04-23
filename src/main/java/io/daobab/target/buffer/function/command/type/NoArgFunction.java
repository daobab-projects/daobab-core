package io.daobab.target.buffer.function.command.type;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Plates;

import java.util.List;
import java.util.Map;

public class NoArgFunction<F> extends BufferFunction<F> {

    @Override
    protected Plates apply(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        return null;
    }

    @Override
    protected final List<Object> applyField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        return null;
    }

    @Override
    public FunctionType getType() {
        return null;
    }
}
