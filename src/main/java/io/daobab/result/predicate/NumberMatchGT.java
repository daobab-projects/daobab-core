package io.daobab.result.predicate;


import io.daobab.result.bytebuffer.BitFieldInteger;
import io.daobab.target.buffer.bytebyffer.EntityByteBuffer;

import java.nio.ByteBuffer;

public class NumberMatchGT extends NumberMatchEQ {

    private final BitFieldInteger bt = new BitFieldInteger();

    public NumberMatchGT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare < (((Number) valueFromEntityField).doubleValue());
    }

    public boolean bitTest(EntityByteBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
//        return test((O) bufferEntityPointer.getValue(entityPosition, columnPositionIntoEntity, colPosition));

        int page = entityPosition >> bufferEntityPointer.pageMaxCapacityBytes;
        int rowAtPage = entityPosition - (page << bufferEntityPointer.pageMaxCapacityBytes);
        int fieldPosition = (rowAtPage * bufferEntityPointer.totalEntitySpace) + colPosition;
        Integer v = bt.readValue((ByteBuffer) bufferEntityPointer.getPages().get(page), fieldPosition);
        return v != null && valueToCompare < v;
    }
}
