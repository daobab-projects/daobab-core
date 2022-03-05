package io.daobab.target.buffer.function.command;

import io.daobab.model.Column;
import io.daobab.target.buffer.single.Plates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;


public class Avg extends Sum {

    public Avg() {
        map.put(Double.class, this::avgDouble);
        map.put(Float.class, this::avgFloat);
        map.put(Long.class, this::avgLong);
        map.put(Integer.class, this::avgInt);
        map.put(BigDecimal.class, this::avgBigDecimal);
        map.put(BigInteger.class, this::avgBigInteger);
        map.put(Short.class, this::avgShort);
        map.put(Byte.class, this::avgByte);
    }

    private Long avgLong(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumLong(plates, finalColumn) / plates.size();
    }

    private Integer avgInt(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumInt(plates, finalColumn) / plates.size();
    }

    private Short avgShort(Plates plates, Column<?, ?, ?> finalColumn) {
        return (short) ((int) sumShort(plates, finalColumn) / plates.size());
    }

    private Byte avgByte(Plates plates, Column<?, ?, ?> finalColumn) {
        return (byte) ((int) sumByte(plates, finalColumn) / plates.size());
    }

    private Float avgFloat(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumFloat(plates, finalColumn) / plates.size();
    }

    private Double avgDouble(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumDouble(plates, finalColumn) / plates.size();
    }

    private BigDecimal avgBigDecimal(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumBigDecimal(plates, finalColumn).divide(new BigDecimal(plates.size()), RoundingMode.HALF_UP);
    }

    private BigInteger avgBigInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return sumBigInteger(plates, finalColumn).divide(BigInteger.valueOf(plates.size()));
    }


    protected Long avgLongField(List<Long> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0L, Long::sum);
    }

    protected Integer avgIntField(List<Integer> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0, Integer::sum);
    }

    protected Short avgShortField(List<Short> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((short) 0, (a, b) -> (short) (a + b));
    }

    protected Byte avgByteField(List<Byte> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (byte) (a + b));
    }

    protected Float avgFloatField(List<Float> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0f, Float::sum);
    }

    protected Double avgDoubleField(List<Double> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0d, Double::sum);
    }

    protected BigDecimal avgBigDecimalField(List<BigDecimal> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::add);
    }

    protected BigInteger avgBigIntegerField(List<BigInteger> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::add);
    }


}
