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

/**
 * The registry of the in-memory {@link BufferFunction}s (keyed by their {@link DictFunctionBuffer} name) and the
 * orchestrator applying them to a buffer. {@link #applyFunctions} runs the aggregated functions over the whole
 * buffer first (collecting their single-row results), then the row-by-row ones, and joins everything into the
 * final result.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BufferFunctionManager extends HashMap<String, BufferFunction> implements FunctionWhispererBuffer {

    /**
     * Registers the supported in-memory buffer functions.
     */
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

    /**
     * Applies the given functions to the buffer: the aggregated ones first (over the whole buffer), then the
     * row-by-row ones, joining the results.
     *
     * @param plates the buffer rows
     * @param map    the functions to apply, by their select-list position
     * @return the transformed buffer
     */
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
            aggregatedPlate.joinPlate(aggregatedPlates.getFirst());
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
            Plate rvPlate = resultPlates.getFirst();
            for (Plates agg : aggregatedResults) {
                if (agg.isEmpty()) {
                    continue; //shouldn't happen
                }
                rvPlate.joinPlate(agg.getFirst());
            }
            rvPlate.joinPlate(aggregatedPlate);
            rv = new PlateBuffer(Collections.singletonList(rvPlate));
        } else if (!aggregatedResults.isEmpty()) {
            Plate plate = new Plate();
            for (Plates agg : aggregatedResults) {
                if (agg.isEmpty()) {
                    continue; //shouldn't happen
                }
                plate.joinPlate(agg.getFirst());
            }

            rv = new PlateBuffer(Collections.singletonList(plate));
        }
        return rv;
    }

    /**
     * Applies the given functions to a flat field list.
     *
     * @throws DaobabException when no handler is registered for a function
     */
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
