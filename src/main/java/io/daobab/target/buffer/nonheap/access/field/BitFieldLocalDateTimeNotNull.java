package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldLocalDateTimeNotNull extends BitFieldTemporal<LocalDateTime> implements BitField<LocalDateTime> {

    public BitFieldLocalDateTimeNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, LocalDateTime val) {
        Instant instant = val.toInstant(ZoneOffset.UTC);
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public LocalDateTime readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        return LocalDateTime.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
    }

    @Override
    public Class<LocalDateTime> getClazz() {
        return LocalDateTime.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DATE_UTIL;
    }


    @Override
    public Comparator<? super LocalDateTime> comparator() {
        return (Comparator<LocalDateTime>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }


}
