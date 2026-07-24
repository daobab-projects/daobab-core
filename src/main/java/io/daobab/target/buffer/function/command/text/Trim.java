package io.daobab.target.buffer.function.command.text;

import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The {@code TRIM} function: strips the leading/trailing whitespace of the string column of each row (string
 * columns only).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Trim extends BufferFunction<String> {

    /**
     * Trims the column value of each row.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Plates rv = getClonedPlates(plates, false);
        for (Plate plate : rv) {
            String val = (String) plate.getValue(function.getFinalColumn());
            if (val != null) {
                plate.setValue(function, val.trim());
            }
        }
        return rv;
    }

    /** Trims each field value ({@code null} preserved). */
    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        List<Object> rv = new ArrayList<>();
        for (Object plate : fields) {
            if (plate != null) {
                rv.add(((String) plate).trim());
            } else {
                rv.add(null);
            }
        }
        return rv;
    }

    /** {@inheritDoc} {@link FunctionType#NORMAL}. */
    @Override
    public FunctionType getType() {
        return FunctionType.NORMAL;
    }

    /** {@inheritDoc} String columns only. */
    @Override
    protected Collection<Class<?>> getSuitableTypes() {
        return STRING_ONLY;
    }
}
