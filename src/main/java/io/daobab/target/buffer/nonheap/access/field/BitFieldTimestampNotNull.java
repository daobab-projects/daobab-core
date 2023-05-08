package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldTimestampNotNull implements BitField<Timestamp> {

    public BitFieldTimestampNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Timestamp val) {
        Instant instant = val.toInstant();
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public Timestamp readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        return Timestamp.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
    }

    @Override
    public Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.TIMESTAMP_SQL;
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
