package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.WherePredicate;
import io.daobab.statement.condition.Operator;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldBlob implements BitField<byte[]> {

    public BitFieldBlob(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, byte[] bytearray) {
        if (bytearray != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.NULL, bytearray.length);
            byteBuffer.position(position + BitSize.NULL + BitSize.INT);
            byteBuffer.put(bytearray);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public byte[] readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return new byte[0];
        }
        int length = byteBuffer.getInt(position + BitSize.NULL);
        byte[] bytearray = new byte[length];
        byteBuffer.position(position + BitSize.NULL + BitSize.INT);
        byteBuffer.get(bytearray);
        return bytearray;
    }

    @Override
    public Class<byte[]> getClazz() {
        return byte[].class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT + BitSize.NULL;
    }

    @Override
    public Comparator<? super byte[]> comparator() {
        return null;
    }

    @Override
    public Function<byte[], WherePredicate<byte[]>> getPredicate(Operator operator) {
        return null;
    }

}
