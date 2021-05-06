package io.daobab.result.predicate;


import io.daobab.result.EntityByteBuffer;

public class IntegerMatchLT extends IntegerMatchEQ {

    public IntegerMatchLT(int valueToCompare) {
        super(valueToCompare);
    }

    public boolean test(Integer valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare > valueFromEntityField;
    }

    public boolean bitTest(EntityByteBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        Integer v = getEntityVal(bufferEntityPointer, entityPosition, columnPositionIntoEntity, colPosition);
        return v != null && valueToCompare > v;
    }
}
