package io.daobab.target.buffer.function.command;

import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.*;

public class Upper extends BufferFunction<String> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Plates apply(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Plates rv = getClonedPlates(plates, false);
        for (Plate plate : rv) {
            String val = (String) plate.getValue(function.getFinalColumn());
            if (val != null) {
                plate.setValue(function, val.toUpperCase(Locale.ROOT));
            }
        }
        return rv;
    }

    @Override
    protected List<Object> applyField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        List<Object> rv = new ArrayList<>();
        for (Object plate : plates) {
            if (plate != null) {
                rv.add(((String) plate).toUpperCase(Locale.ROOT));
            } else {
                rv.add(null);
            }
        }
        return rv;
    }

    @Override
    public FunctionType getType() {
        return FunctionType.NORMAL;
    }

    @Override
    protected Collection<Class<?>> getSuitableTypes() {
        return STRING_ONLY;
    }
}
