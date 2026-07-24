package io.daobab.target.buffer.function.command;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code COUNT(column)} aggregate: counts the non-null values of the column across the buffer.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Count extends BufferFunction<Object> {

    /**
     * Collapses the buffer to one row holding the count of the column's non-null values.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Long count = plates.stream().map(p -> p.getValue(function.getFinalColumn())).filter(Objects::nonNull).count();
        Plates rv = getClonedPlates(plates, true);
        rv.get(0).setValue(function, count);
        return rv;
    }

    /** The count of the non-null field values. */
    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        return Collections.singletonList(fields.stream().filter(Objects::nonNull).count());
    }

    /** {@inheritDoc} {@link FunctionType#AGGREGATED}. */
    @Override
    public FunctionType getType() {
        return FunctionType.AGGREGATED;
    }

}
