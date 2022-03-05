package io.daobab.target.buffer.function.command;

import io.daobab.model.Column;
import io.daobab.target.buffer.single.Plates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;


public class Max extends Sum {

    public Max() {
        map.put(Double.class, this::maxDouble);
        map.put(Float.class, this::maxFloat);
        map.put(Long.class, this::maxLong);
        map.put(Integer.class, this::maxInt);
        map.put(BigDecimal.class, this::maxBigDecimal);
        map.put(BigInteger.class, this::maxBigInteger);
        map.put(Short.class, this::maxShort);
        map.put(Byte.class, this::maxByte);
    }

    private Long maxLong(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Long) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0L, Long::max);
    }

    private Integer maxInt(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Integer) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0, Integer::max);
    }

    private Short maxShort(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Short) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((short) 0, (a, b) -> (a > b) ? a : b);
    }

    private Byte maxByte(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Byte) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (a > b) ? a : b);
    }

    private Float maxFloat(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Float) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0f, Float::max);
    }

    private Double maxDouble(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Double) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0d, Double::max);
    }

    private BigDecimal maxBigDecimal(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigDecimal) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::max);
    }

    private BigInteger maxBigInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigInteger) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::max);
    }


    protected Long maxLongField(List<Long> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0L, Long::max);
    }

    protected Integer maxIntField(List<Integer> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0, Integer::max);
    }

    protected Short maxShortField(List<Short> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((short) 0, (a, b) -> (short) (a + b));
    }

    protected Byte maxByteField(List<Byte> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (byte) (a + b));
    }

    protected Float maxFloatField(List<Float> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0f, Float::max);
    }

    protected Double maxDoubleField(List<Double> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0d, Double::max);
    }

    protected BigDecimal maxBigDecimalField(List<BigDecimal> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::max);
    }

    protected BigInteger maxBigIntegerField(List<BigInteger> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::max);
    }


}
