package io.daobab.target.buffer.function.command;

import io.daobab.model.Column;
import io.daobab.target.buffer.single.Plates;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;


public class Min extends Sum {

    public Min() {
        map.put(Double.class, this::minDouble);
        map.put(Float.class, this::minFloat);
        map.put(Long.class, this::minLong);
        map.put(Integer.class, this::minInt);
        map.put(BigDecimal.class, this::minBigDecimal);
        map.put(BigInteger.class, this::minBigInteger);
        map.put(Short.class, this::minShort);
        map.put(Byte.class, this::minByte);
    }

    private Long minLong(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Long) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0L, Long::min);
    }

    private Integer minInt(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Integer) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0, Integer::min);
    }

    private Short minShort(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Short) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((short) 0, (a, b) -> (a < b) ? a : b);
    }

    private Byte minByte(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Byte) p.getValue(finalColumn)).filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (a < b) ? a : b);
    }

    private Float minFloat(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Float) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0f, Float::min);
    }

    private Double minDouble(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (Double) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(0d, Double::min);
    }

    private BigDecimal minBigDecimal(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigDecimal) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::min);
    }

    private BigInteger minBigInteger(Plates plates, Column<?, ?, ?> finalColumn) {
        return plates.stream().map(p -> (BigInteger) p.getValue(finalColumn)).filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::min);
    }


    protected Long minLongField(List<Long> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0L, Long::min);
    }

    protected Integer minIntField(List<Integer> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0, Integer::min);
    }

    protected Short minShortField(List<Short> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((short) 0, (a, b) -> (short) (a + b));
    }

    protected Byte minByteField(List<Byte> plates) {
        return plates.stream().filter(Objects::nonNull).reduce((byte) 0, (a, b) -> (byte) (a + b));
    }

    protected Float minFloatField(List<Float> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0f, Float::min);
    }

    protected Double minDoubleField(List<Double> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(0d, Double::min);
    }

    protected BigDecimal minBigDecimalField(List<BigDecimal> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(new BigDecimal(0), BigDecimal::min);
    }

    protected BigInteger minBigIntegerField(List<BigInteger> plates) {
        return plates.stream().filter(Objects::nonNull).reduce(BigInteger.valueOf(0), BigInteger::min);
    }


}
