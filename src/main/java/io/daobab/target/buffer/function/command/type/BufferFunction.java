package io.daobab.target.buffer.function.command.type;

import io.daobab.error.DaobabException;
import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;

import java.util.*;

public abstract class BufferFunction<F> {

    protected Collection<Class<?>> STRING_ONLY = Collections.singletonList(String.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Plates execute(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChild(manager, plates, function);
        return apply(manager, plates, function);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<?> executeField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        if (!isSuitableFor(function.getFinalColumn().getFieldClass())) {
            throw new DaobabException("Function %s is not allowed for type %s", function.getMode(), function.getFinalColumn().getFieldClass().getSimpleName());
        }
        handleChildField(manager, plates, function);
        return applyField(manager, plates, function);
    }

    @SuppressWarnings("rawtypes")
    protected abstract Plates apply(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function);

    @SuppressWarnings("rawtypes")
    protected abstract List<Object> applyField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function);

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleChild(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        if (function.hasChild()) {
            ColumnFunction childFunction = function.getChild();
            BufferFunction bufferFunction = manager.get(childFunction.getMode());
            if (bufferFunction != null) {
                bufferFunction.apply(manager, plates, childFunction);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleChildField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        if (function.hasChild()) {
            ColumnFunction childFunction = function.getChild();
            BufferFunction bufferFunction = manager.get(childFunction.getMode());
            if (bufferFunction != null) {
                bufferFunction.applyField(manager, plates, childFunction);
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

    public abstract FunctionType getType();
}
