package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.function.Function;

public class BitFieldTimestamp implements BitField<Timestamp> {

    public BitFieldTimestamp(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Timestamp val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Instant instant = val.toInstant();
            byteBuffer.putLong(position + BitSize.NULL, instant.getEpochSecond());
            byteBuffer.putInt(position + BitSize.NULL + BitSize.LONG, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Timestamp readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + BitSize.NULL)));
        return Timestamp.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.NULL + BitSize.LONG)));
    }

    @Override
    public Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG + BitSize.INT + BitSize.NULL;
    }


    @Override
    public Comparator<? super Timestamp> comparator() {
        return (Comparator<Timestamp>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

    @Override
    public Function<Timestamp, WherePredicate<Timestamp>> getPredicate(Operator operator) {
        return null;
    }
}
