package io.daobab.result.predicate;


import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.nonheap.access.field.BitFieldInteger;

import java.nio.ByteBuffer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NumberMatchGT extends NumberMatchEQ {

    private final BitFieldInteger bt = new BitFieldInteger();

    public NumberMatchGT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare < (((Number) valueFromEntityField).doubleValue());
    }

    public boolean bitTest(NonHeapEntities bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {

        int page = entityPosition >> bufferEntityPointer.pageMaxCapacityBytes;
        int rowAtPage = entityPosition - (page << bufferEntityPointer.pageMaxCapacityBytes);
        int fieldPosition = (rowAtPage * bufferEntityPointer.totalEntitySpace) + colPosition;
        Integer v = bt.readValue((ByteBuffer) bufferEntityPointer.getPages().get(page), fieldPosition);
        return v != null && valueToCompare < v;
    }
}
