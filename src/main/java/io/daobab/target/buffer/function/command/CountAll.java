package io.daobab.target.buffer.function.command;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The {@code COUNT(*)} aggregate: counts the buffer rows (regardless of any column value).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CountAll extends BufferFunction<Object> {

    /**
     * Collapses the buffer to one row holding the number of rows.
     */
    @SuppressWarnings("rawtypes")
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Plates rv = getClonedPlates(plates, true);
        rv.get(0).setValue(function, plates.size());
        return rv;
    }

    /** The number of field values. */
    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        return Collections.singletonList(fields.size());
    }

    /** {@inheritDoc} {@link FunctionType#AGGREGATED}. */
    @Override
    public FunctionType getType() {
        return FunctionType.AGGREGATED;
    }

}
