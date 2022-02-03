package io.daobab.result.predicate;


import io.daobab.target.buffer.bytebyffer.EntityByteBuffer;

public class IntegerMatchGTEQ extends IntegerMatchEQ {

    public IntegerMatchGTEQ(int valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Integer valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare <= valueFromEntityField;
    }

    public boolean bitTest(EntityByteBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        Integer v = getEntityVal(bufferEntityPointer, entityPosition, columnPositionIntoEntity, colPosition);
        return v != null && valueToCompare <= v;
    }
}
