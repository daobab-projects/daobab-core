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
public class BitFieldLocalDateTime extends BitFieldTemporal<LocalDateTime> implements BitField<LocalDateTime> {

    public BitFieldLocalDateTime(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, LocalDateTime val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Instant instant = val.toInstant(ZoneOffset.UTC);

            byteBuffer.putLong(position + BitSize.NULL, instant.getEpochSecond());
            byteBuffer.putInt(position + BitSize.NULL + BitSize.LONG, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public LocalDateTime readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + BitSize.NULL)))
                .plusNanos(byteBuffer.getInt(position + BitSize.NULL + BitSize.LONG));
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public Class<LocalDateTime> getClazz() {
        return LocalDateTime.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.NULL + BitSize.DATE_UTIL;
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
