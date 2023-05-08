package io.daobab.target.buffer.function.command.date;

import io.daobab.model.Column;
import io.daobab.model.Plate;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.function.command.type.BufferFunction;
import io.daobab.target.buffer.function.command.type.FunctionType;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Year extends BufferFunction<Object> {

    protected Map<Class<?>, BiFunction<Plates, Column<?, ?, ?>, ?>> map = new HashMap<>();
    @SuppressWarnings("rawtypes")
    protected Map<Class<?>, Function<List, ?>> mapField = new HashMap<>();

    public Year() {
        map.put(Double.class, this::fieldDouble);
        map.put(Float.class, this::fieldFloat);
        map.put(Long.class, this::fieldLong);
        map.put(Integer.class, this::fieldInteger);
        map.put(BigDecimal.class, this::fieldBigDecimal);
        map.put(BigInteger.class, this::fieldBigInteger);
        map.put(Short.class, this::fieldShort);

//        mapField.put(Double.class, this::fieldDoubleField);
//        mapField.put(Float.class, this::fieldFloatField);
//        mapField.put(Long.class, this::fieldLongField);
//        mapField.put(Integer.class, this::fieldIntField);
//        mapField.put(BigDecimal.class, this::fieldBigDecimalField);
//        mapField.put(BigInteger.class, this::fieldBigIntegerField);
//        mapField.put(Short.class, this::fieldShortField);
    }

    @SuppressWarnings("rawtypes")
    protected Plates applyOnPlates(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Column<?, ?, ?> finalColumn = function.getFinalColumn();
        BiFunction<Plates, Column<?, ?, ?>, ?> function2 = map.get(function.getFinalColumn().getFieldClass());
        Plate rv = new Plate();
        rv.setValue(function, function2.apply(plates, finalColumn));
        return new PlateBuffer(Collections.singletonList(rv));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List<Object> applyOnFields(Map<String, BufferFunction> manager, List<?> fields, ColumnFunction<?, ?, ?, ?> function) {
        Class<?> clazz = readClass(fields);
        if (clazz == null) {
            return Collections.emptyList();
        }

        Function<List, ?> function2 = mapField.get(clazz);
        return Collections.singletonList(function2.apply(fields));
    }

    private int getYearAsInteger() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    protected Long fieldLong(Plates plates, Column<?, ?, ?> finalColumn) {
        return (long) getYearAsInteger();
    }

    protected Integer fieldInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return getYearAsInteger();
    }

    protected Double fieldDouble(Plates plates, Column<?, ?, ?> finalColumn) {
        return (double) getYearAsInteger();
    }

    protected Float fieldFloat(Plates plates, Column<?, ?, ?> finalColumn) {
        return (float) getYearAsInteger();
    }

    protected Short fieldShort(Plates plates, Column<?, ?, ?> finalColumn) {
        return (short) getYearAsInteger();
    }

    protected BigDecimal fieldBigDecimal(Plates plates, Column<?, ?, ?> finalColumn) {
        return new BigDecimal(getYearAsInteger());
    }

    protected BigInteger fieldBigInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return BigInteger.valueOf(getYearAsInteger());
    }


    @Override
    public FunctionType getType() {
        return FunctionType.NORMAL;
    }


}
