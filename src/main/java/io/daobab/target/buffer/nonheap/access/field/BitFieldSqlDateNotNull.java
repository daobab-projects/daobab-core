package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.time.Instant;
import java.util.Comparator;
import java.util.function.Function;

public class BitFieldSqlDateNotNull implements BitField<Date> {

    public BitFieldSqlDateNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        Instant instant = val.toInstant();
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        java.util.Date utilDate = java.util.Date.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
        return new Date(utilDate.getTime());
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DATE_SQL;
    }


    @Override
    public Comparator<? super Date> comparator() {
        return (Comparator<Date>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

    @Override
    public Function<Date, WherePredicate<Date>> getPredicate(Operator operator) {
        return null;
    }
}
