package io.daobab.target.buffer.function.command;

import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Distinct extends BufferFunction<Object> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        List<Plate> filtered = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Plate value : plates) {
            Plate plate = new Plate(value, true);
            Object val = plate.getValue(function.getFinalColumn());
            if (!values.contains(val)) {
                filtered.add(plate);
                values.add(val);
            }
        }
        return new PlateBuffer(filtered);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        List<Object> filtered = new ArrayList<>();

        for (Object value : fields) {
            if (!filtered.contains(value)) {
                filtered.add(value);
            }
        }
        return filtered;
    }


    @Override
    public FunctionType getType() {
        return FunctionType.NORMAL;
    }

}
