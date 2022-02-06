package io.daobab.result.predicate;

import io.daobab.target.buffer.noheap.NoHeapEntities;
import io.daobab.target.buffer.noheap.access.BitFieldInteger;

import java.nio.ByteBuffer;

public class IntegerMatchEQ implements WherePredicate<Integer> {

    protected final int valueToCompare;
    protected final BitFieldInteger bt;


    public IntegerMatchEQ(int valueToCompare) {
        this.valueToCompare = valueToCompare;
        bt = new BitFieldInteger();
    }

    @Override
    public boolean test(Integer valueFromEntityField) {
        return valueToCompare == valueFromEntityField;
    }

    protected Integer getEntityVal(NoHeapEntities bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        int page = entityPosition >> bufferEntityPointer.pageMaxCapacityBytes;
        int rowAtPage = entityPosition - (page << bufferEntityPointer.pageMaxCapacityBytes);
        int fieldPosition = (rowAtPage * bufferEntityPointer.totalEntitySpace) + colPosition;
        return bt.readValue((ByteBuffer) bufferEntityPointer.getPages().get(page), fieldPosition);
    }


}
