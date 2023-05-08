package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldShortNotNull extends BitFieldComparable<Short> {

    public BitFieldShortNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Short val) {
        byteBuffer.putShort(position, val);
    }

    @Override
    public Short readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getShort(position);
    }

    @Override
    public Class<Short> getClazz() {
        return Short.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.SHORT;
    }

    @Override
    public Comparator<? super Short> comparator() {
        return Comparator.comparing(Short::valueOf);
    }

}
