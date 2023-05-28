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
public class BitFieldLocalDate extends BitFieldTemporal<LocalDate> implements BitField<LocalDate> {

    public BitFieldLocalDate(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, LocalDate val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Instant instant = val.atStartOfDay().toInstant(ZoneOffset.UTC);

            byteBuffer.putLong(position + BitSize.NULL, instant.getEpochSecond());
            byteBuffer.putInt(position + BitSize.NULL + BitSize.LONG, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public LocalDate readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + BitSize.NULL)));
        return LocalDate.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.NULL + BitSize.LONG)));
    }

    @Override
    public Class<LocalDate> getClazz() {
        return LocalDate.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.NULL + BitSize.DATE_UTIL;
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
