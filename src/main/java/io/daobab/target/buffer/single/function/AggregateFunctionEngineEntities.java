package io.daobab.target.buffer.single.function;

import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.function.aggregate.*;

import java.util.HashMap;
import java.util.Map;

public class AggregateFunctionEngineEntities {

    public static AggregateFunctionEngineEntities INSTANCE = new AggregateFunctionEngineEntities();
    private final Map<String, AggregateFunction> dictionary;
    private final FunctionTypeConverter functionTypeConverter;

    private AggregateFunctionEngineEntities() {
        dictionary = new HashMap<>();
        addFunction(new FunctionCount());
        addFunction(new FunctionSum());
        addFunction(new FunctionAvg());
        addFunction(new FunctionMin());
        addFunction(new FunctionMax());
        functionTypeConverter = new FunctionTypeConverter();
    }

    private void addFunction(AggregateFunction function) {
        dictionary.put(function.mode(), function);
    }


    public String applyFunction(Entities target, ColumnFunction columnFunction) {
        AggregateFunction function = dictionary.get(columnFunction.getMode());
        if (function == null) return null;
        return function.applyFunction(target, columnFunction);
    }

    public <F> F executeFunction(Entities target, ColumnFunction<?, F, ?, ?> columnFunction) {

        String result = applyFunction(target, columnFunction);
        if (result == null) {
            return null;
        }

        if (columnFunction.getFieldClass().equals(String.class)) {
            return (F) result;
        } else if (columnFunction.getFieldClass().equals(Long.class)) {
            return (F) functionTypeConverter.toLong(result);
        }

        return null;
    }
}
