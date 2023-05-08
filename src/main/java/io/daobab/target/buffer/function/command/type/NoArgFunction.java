package io.daobab.target.buffer.function.command.type;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Plates;

import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoArgFunction<F> extends BufferFunction<F> {

    @Override
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        return null;
    }

    @Override
    protected final List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        return null;
    }

    @Override
    public FunctionType getType() {
        return null;
    }
}
