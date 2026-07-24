package io.daobab.target.buffer.function.command.date;

import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.Plates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The {@code CURRENT_DATE} function: sets the current date on each row.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CurrentDate extends BufferFunction<Object> {

    /**
     * Sets the current date as the function's value on each row.
     */
    @SuppressWarnings("rawtypes")
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Plates rv = getClonedPlates(plates, false);
        Date currDate = new Date();
        for (Plate plate : rv) {
            plate.setValue(function, currDate);
        }
        return rv;
    }

    /** The current date for each field value ({@code null} preserved). */
    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        List<Object> rv = new ArrayList<>();
        Date currDate = new Date();
        for (Object plate : fields) {
            if (plate != null) {
                rv.add(currDate);
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


}
