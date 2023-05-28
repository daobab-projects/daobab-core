package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldLocalDateNotNull extends BitFieldTemporal<LocalDate> implements BitField<LocalDate> {

    public BitFieldLocalDateNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, LocalDate val) {
        Instant instant = val.atStartOfDay().toInstant(ZoneOffset.UTC);
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public LocalDate readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        return LocalDate.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
    }

    @Override
    public Class<LocalDate> getClazz() {
        return LocalDate.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DATE_UTIL;
    }


    @Override
    public Comparator<? super LocalDate> comparator() {
        return (Comparator<LocalDate>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }


}
