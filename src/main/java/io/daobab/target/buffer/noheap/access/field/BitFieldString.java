package io.daobab.target.buffer.noheap.access.field;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;
import io.daobab.result.predicate.MatchStringLike;
import io.daobab.result.predicate.MatchStringNotLike;
import io.daobab.result.predicate.WherePredicate;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.function.Function;

public class BitFieldString extends BitFieldComparable<String> {

    public BitFieldString(TableColumn tableColumn) {

    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, String val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);

            byteBuffer.putInt(position + BitSize.NULL, val.length());
            byteBuffer.position(position + BitSize.INT + BitSize.NULL);
            byteBuffer.put(val.getBytes(StandardCharsets.UTF_8));
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public String readValue(ByteBuffer byteBuffer, Integer position) {

        byte isNull = byteBuffer.get(position);
        if (isNull == 0) {
            return null;
        }
        byteBuffer.position(position + BitSize.NULL);
        byte[] read = new byte[byteBuffer.getInt()];
        byteBuffer.position(position + BitSize.INT + BitSize.NULL);
        byteBuffer.get(read);
        return new String(read, StandardCharsets.UTF_8);
    }

    @Override
    public Class<String> getClazz() {
        return String.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        if (column.getSize() == 0) {
            throw new DaobabException("Field " + column.getColumn().toString() + " needs size specified.");
        }
        //max utf-8 space has to be multiplied by 6 (worst case scenario)
//        return column.getSize() * 6 + BitSize.INT + BitSize.CHECK_NULL;
        return BitSize.calculateStringSize(column.getSize(), true);
    }


    @Override
    public Comparator<? super String> comparator() {
        return (Comparator<String>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

    @Override
    public Function<String, WherePredicate<String>> getPredicateLike() {
        return MatchStringLike::new;
    }

    @Override
    public Function<String, WherePredicate<String>> getPredicateNotLike() {
        return MatchStringNotLike::new;
    }
}
