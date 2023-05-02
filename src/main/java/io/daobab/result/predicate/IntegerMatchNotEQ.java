package io.daobab.result.predicate;


import io.daobab.target.buffer.nonheap.NonHeapEntities;

public class IntegerMatchNotEQ extends IntegerMatchEQ {

    public IntegerMatchNotEQ(int valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Integer valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare != valueFromEntityField;
    }

    public boolean bitTest(NonHeapEntities bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        Integer v = getEntityVal(bufferEntityPointer, entityPosition, columnPositionIntoEntity, colPosition);
        return v != null && valueToCompare != v;
    }
}
