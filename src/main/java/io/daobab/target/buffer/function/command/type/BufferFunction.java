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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class BufferFunction<F> {


    protected Map<Class<?>, BiFunction<Plates, Column<?, ?, ?>, ?>> map = new HashMap<>();
    @SuppressWarnings("rawtypes")
    protected Map<Class<?>, Function<List, ?>> mapField = new HashMap<>();

    protected Collection<Class<?>> STRING_ONLY = Collections.singletonList(String.class);
    protected Collection<Class<?>> ALL_DATES = Arrays.asList(Date.class, java.sql.Date.class, Timestamp.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Plates execute(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChild(manager, plates, function);
        return applyOnPlates(manager, plates, function);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<?> executeField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChildField(manager, plates, function);
        return applyOnFields(manager, plates, function);
    }

    @SuppressWarnings("rawtypes")
    protected abstract Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function);

    //    @SuppressWarnings("rawtypes")
    protected abstract List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function);

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

    protected Collection<Class<?>> getSuitableTypes() {
        return null;
    }

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


    protected Class<?> readClass(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        for (Object obj : list) {
            if (obj != null) {
                return obj.getClass();
            }
        }
        return null;
    }

    public abstract FunctionType getType();
}
