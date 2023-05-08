package io.daobab.result.predicate;

import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.nonheap.access.field.BitFieldInteger;

import java.nio.ByteBuffer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TwoFieldsMatchEQ implements WherePredicate<Integer> {

    protected final int valueToCompare;
    protected final BitFieldInteger bt;


    public TwoFieldsMatchEQ(int valueToCompare) {
        this.valueToCompare = valueToCompare;
        bt = new BitFieldInteger();
    }


    public boolean test(Integer valueFromEntityField) {
        return valueToCompare == valueFromEntityField;
    }

    protected Integer getEntityVal(NonHeapEntities bufferEntityPointer, int entityPosition, int colPosition) {
        int page = entityPosition >> bufferEntityPointer.pageMaxCapacityBytes;
        int rowAtPage = entityPosition - (page << bufferEntityPointer.pageMaxCapacityBytes);
        int fieldPosition = (rowAtPage * bufferEntityPointer.totalEntitySpace) + colPosition;
        return bt.readValue((ByteBuffer) bufferEntityPointer.getPages().get(page), fieldPosition);
    }


}
