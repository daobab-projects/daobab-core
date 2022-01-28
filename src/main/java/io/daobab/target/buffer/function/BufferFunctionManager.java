package io.daobab.target.buffer.function;

import io.daobab.error.DaobabException;
import io.daobab.model.Plate;
import io.daobab.statement.function.dictionary.DictFunctionBuffer;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.*;
import io.daobab.target.buffer.function.command.date.CurrentDate;
import io.daobab.target.buffer.function.command.date.Year;
import io.daobab.target.buffer.function.command.text.Length;
import io.daobab.target.buffer.function.command.text.Lower;
import io.daobab.target.buffer.function.command.text.Trim;
import io.daobab.target.buffer.function.command.text.Upper;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;

import java.util.*;

public class BufferFunctionManager extends HashMap<String, BufferFunction> implements FunctionWhispererBuffer {

    public BufferFunctionManager() {
        put(DictFunctionBuffer.LENGTH, new Length());
        put(DictFunctionBuffer.COUNT, new Count());
        put(DictFunctionBuffer.DISTINCT, new Distinct());
        put(DictFunctionBuffer.UPPER, new Upper());
        put(DictFunctionBuffer.LOWER, new Lower());
        put(DictFunctionBuffer.SUM, new Sum());
        put(DictFunctionBuffer.MIN, new Min());
        put(DictFunctionBuffer.MAX, new Max());
        put(DictFunctionBuffer.TRIM, new Trim());
        put(DictFunctionBuffer.CURRENT_DATE, new CurrentDate());
        put(DictFunctionBuffer.YEAR, new Year());
    }

    public Plates applyFunctions(Plates plates, Map<Integer, ColumnFunction<?, ?, ?, ?>> map) {
        if (map.isEmpty()) {
            return plates;
        }

        List<Plates> aggregatedResults = new ArrayList<>();

        //at first, execute aggregated function on the entire buffer and collect the single results
        for (Map.Entry<Integer, ColumnFunction<?, ?, ?, ?>> entry : map.entrySet()) {
            BufferFunction<?> bufferFunction = this.get(entry.getValue().getMode());
            if (bufferFunction != null && bufferFunction.getType().equals(FunctionType.AGGREGATED)) {
                aggregatedResults.add(bufferFunction.execute(this, plates, entry.getValue()));
            }
        }

        Plate aggregatedPlate = new Plate();
        for (Plates aggregatedPlates : aggregatedResults) {
            aggregatedPlate.joinPlate(aggregatedPlates.get(0));
        }

        Plates resultPlates = plates;
        boolean nonAggregativeWereInUse = false;

        for (Map.Entry<Integer, ColumnFunction<?, ?, ?, ?>> entry : map.entrySet()) {
            BufferFunction<?> bufferFunction = this.get(entry.getValue().getMode());
            if (bufferFunction != null && !bufferFunction.getType().equals(FunctionType.AGGREGATED)) {
                resultPlates = bufferFunction.execute(this, resultPlates, entry.getValue());
                nonAggregativeWereInUse = true;
            }
        }

        Plates rv = resultPlates;

        if (!aggregatedResults.isEmpty() && nonAggregativeWereInUse) {
            Plate rvPlate = resultPlates.get(0);
            for (Plates agg : aggregatedResults) {
                if (agg.isEmpty()) {
                    continue; //shouldn't happen
                }
                rvPlate.joinPlate(agg.get(0));
            }
            rvPlate.joinPlate(aggregatedPlate);
            rv = new PlateBuffer(Collections.singletonList(rvPlate));
        } else if (!aggregatedResults.isEmpty()) {
            Plate plate = new Plate();
            for (Plates agg : aggregatedResults) {
                if (agg.isEmpty()) {
                    continue; //shouldn't happen
                }
                plate.joinPlate(agg.get(0));
            }

            rv = new PlateBuffer(Collections.singletonList(plate));
        }
        return rv;
    }

    public List applyFunctionsField(List<?> plates, Map<Integer, ColumnFunction<?, ?, ?, ?>> map) {
        if (map.isEmpty()) {
            return plates;
        }

        //at first, execute aggregated function on the entire buffer and collect the single results
        for (Map.Entry<Integer, ColumnFunction<?, ?, ?, ?>> entry : map.entrySet()) {
            BufferFunction<?> bufferFunction = get(entry.getValue().getMode());
            if (bufferFunction == null) {
                throw new DaobabException("Cannot find handler for function %s", entry.getValue().getMode());
            }
            return bufferFunction.executeField(this, plates, entry.getValue());
        }
        return Collections.emptyList();
    }
}
