package io.daobab.target.buffer.function.command;

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


public class Sum extends BufferFunction<Object> {

    protected Map<Class<?>, BiFunction<Plates, Column<?, ?, ?>, ?>> map = new HashMap<>();
    protected Map<Class<?>, Function<List, ?>> mapField = new HashMap<>();

    public Sum() {
        map.put(Double.class, this::sumDouble);
        map.put(Float.class, this::sumFloat);
        map.put(Long.class, this::sumLong);
        map.put(Integer.class, this::sumInt);
        map.put(BigDecimal.class, this::sumBigDecimal);
        map.put(BigInteger.class, this::sumBigInteger);
        map.put(Short.class, this::sumShort);
        map.put(Byte.class, this::sumByte);

        mapField.put(Double.class, this::sumDoubleField);
        mapField.put(Float.class, this::sumFloatField);
        mapField.put(Long.class, this::sumLongField);
        mapField.put(Integer.class, this::sumIntField);
        mapField.put(BigDecimal.class, this::sumBigDecimalField);
        mapField.put(BigInteger.class, this::sumBigIntegerField);
        mapField.put(Short.class, this::sumShortField);
        mapField.put(Byte.class, this::sumByteField);
    }

    @SuppressWarnings("rawtypes")
    protected Plates apply(Map<String, BufferFunction> manager, Plates plates, ColumnFunction<?, ?, ?, ?> function) {
        Column<?, ?, ?> finalColumn = function.getFinalColumn();
        BiFunction<Plates, Column<?, ?, ?>, ?> function2 = map.get(function.getFinalColumn().getFieldClass());
        Plate rv = new Plate();
        rv.setValue(function, function2.apply(plates, finalColumn));
        return new PlateBuffer(Collections.singletonList(rv));
    }

    @Override
    protected List<Object> applyField(Map<String, BufferFunction> manager, List<?> plates, ColumnFunction<?, ?, ?, ?> function) {
        Class<?> clazz = readClass(plates);
        if (clazz == null) {
            return Collections.singletonList(0);
        }
        Function<List, ?> function2 = mapField.get(clazz);
        return Collections.singletonList(function2.apply(plates));
    }

    private Class<?> readClass(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        for (Object obj : list) {
            if (obj != null) {
                return obj.getClass();
            }
        }
        return null;
    }


    @Override
    public FunctionType getType() {
        return FunctionType.AGGREGATED;
    }

    protected Long sumLong(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Long) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0L, Long::sum);
    }

    protected Integer sumInt(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Integer) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0, Integer::sum);
    }

    protected Short sumShort(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Short) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((short) 0, (a, b) -> (short) (a + b));
    }

    protected Byte sumByte(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Byte) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (byte) (a + b));
    }

    protected Float sumFloat(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Float) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0f, Float::sum);
    }

    protected Double sumDouble(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Double) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0d, Double::sum);
    }

    protected BigDecimal sumBigDecimal(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigDecimal) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::add);
    }

    protected BigInteger sumBigInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigInteger) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::add);
    }


    protected Long sumLongField(List<Long> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0L, Long::sum);
    }

    protected Integer sumIntField(List<Integer> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0, Integer::sum);
    }

    protected Short sumShortField(List<Short> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((short) 0, (a, b) -> (short) (a + b));
    }

    protected Byte sumByteField(List<Byte> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (byte) (a + b));
    }

    protected Float sumFloatField(List<Float> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0f, Float::sum);
    }

    protected Double sumDoubleField(List<Double> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0d, Double::sum);
    }

    protected BigDecimal sumBigDecimalField(List<BigDecimal> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::add);
    }

    protected BigInteger sumBigIntegerField(List<BigInteger> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::add);
    }


    @Override
    protected Collection<Class<?>> getSuitableTypes() {
        return map.keySet();
    }
}
