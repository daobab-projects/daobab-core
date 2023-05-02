package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.Comparator;
import java.util.function.Function;

public class BitFieldSqlDate implements BitField<Date> {

    public BitFieldSqlDate(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putLong(position + BitSize.NULL, val.getTime());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return new Date(byteBuffer.getLong(position + BitSize.NULL));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG + BitSize.NULL;
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
