package io.daobab.target.buffer.function.command.type;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The in-memory implementation of a SQL function applied over a buffer: it transforms a set of {@link Plates}
 * rows (or a flat list of field values) according to a {@link ColumnFunction}. Subclasses implement
 * {@link #applyOnPlates}/{@link #applyOnFields} and declare their {@link #getType() type} - {@code AGGREGATED}
 * (collapses the buffer to one row) or {@code NORMAL} (row by row). This is the dialect-agnostic counterpart of
 * the SQL function whisperers, dispatched by name through the {@link io.daobab.target.buffer.function.BufferFunctionManager}.
 *
 * @param <F> the value type the function operates on
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class BufferFunction<F> {


    /**
     * Per-column-type implementations over the buffer rows.
     */
    protected Map<Class<?>, BiFunction<Plates, Column<?, ?, ?>, ?>> map = new HashMap<>();
    /** Per-value-type implementations over a flat field list. */
    @SuppressWarnings("rawtypes")
    protected Map<Class<?>, Function<List, ?>> mapField = new HashMap<>();

    /** The types a string-only function accepts. */
    protected Collection<Class<?>> STRING_ONLY = Collections.singletonList(String.class);
    /** The date/time types. */
    protected Collection<Class<?>> ALL_DATES = Arrays.asList(Date.class, java.sql.Date.class, Timestamp.class);

    /**
     * Runs the function over the plate rows, first resolving any nested child function.
     *
     * @throws DaobabException when the function is not suitable for the column's type
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Plates execute(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChild(manager, plates, function);
        return applyOnPlates(manager, plates, function);
    }

    /**
     * Runs the function over a flat list of field values, first resolving any nested child function.
     *
     * @throws DaobabException when the function is not suitable for the column's type
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<?> executeField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChildField(manager, plates, function);
        return applyOnFields(manager, plates, function);
    }

    /** Applies the function to the plate rows. */
    @SuppressWarnings("rawtypes")
    protected abstract Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function);

    /** Applies the function to a flat list of field values. */
    //    @SuppressWarnings("rawtypes")
    protected abstract List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function);

    /** Runs the nested child function (when the function wraps another) over the plate rows first. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleChild(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        if (function.hasChild()) {
            ColumnFunction childFunction = function.getChild();
            BufferFunction bufferFunction = manager.get(childFunction.getMode());
            if (bufferFunction != null) {
                bufferFunction.applyOnPlates(manager, plates, childFunction);
            }
        }
    }

    /** Runs the nested child function (when the function wraps another) over the field values first. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleChildField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        if (function.hasChild()) {
            ColumnFunction childFunction = function.getChild();
            BufferFunction bufferFunction = manager.get(childFunction.getMode());
            if (bufferFunction != null) {
                bufferFunction.applyOnFields(manager, plates, childFunction);
            }
        }
    }

    private boolean isSuitableFor(Class<F> clazz) {
        Collection<Class<?>> suitableFor = getSuitableTypes();
        return suitableFor == null || suitableFor.contains(clazz);
    }

    /** The column types this function accepts, or {@code null} for all types. */
    protected Collection<Class<?>> getSuitableTypes() {
        return null;
    }

    /** A working copy of the plates: a single row for an aggregate function, otherwise one per input row. */
    protected Plates getClonedPlates(Plates plates, boolean aggregateFunction) {
        List<Plate> rv = new ArrayList<>(aggregateFunction ? 1 : plates.size());
        if (aggregateFunction && plates.isEmpty()) {
            rv.add(new Plate());
        } else {
            for (int i = 0; i < (aggregateFunction ? 1 : plates.size()); i++) {
                rv.add(new Plate(plates.get(i), true));
            }
        }
        return new PlateBuffer(rv);
    }


    /** The class of the first non-null element of the list, or {@code null} when there is none. */
    protected Class<?> readClass(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        for (Object obj : list) {
            if (obj != null) {
                return obj.getClass();
            }
        }
        return null;
    }

    /** Whether this function is {@code AGGREGATED} or applied {@code NORMAL} (row by row). */
    public abstract FunctionType getType();
}
