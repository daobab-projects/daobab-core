package io.daobab.result.predicate;

import io.daobab.result.BaseByteBuffer;
import io.daobab.result.EntityByteBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitStringMatchEQ extends MatchEQ {
    private final EntityByteBuffer bufferEntityPointer;

    private final String strValueToCompare;
    private final int strValueToCompareSize;

    public BitStringMatchEQ(Object valueToCompare, EntityByteBuffer bufferEntityPointer) {
        super(valueToCompare);
        this.strValueToCompare = (String) valueToCompare;
        this.strValueToCompareSize = strValueToCompare.length();
        this.bufferEntityPointer = bufferEntityPointer;
    }

    @Override
    public boolean bitTest(BaseByteBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        int page = entityPosition >> bufferEntityPointer.pageMaxCapacityBytes;
        int rowAtPage = entityPosition - (page << bufferEntityPointer.pageMaxCapacityBytes);
        int posEntity = rowAtPage * bufferEntityPointer.totalEntitySpace;

//        int page = entityPosition / bufferEntityPointer.wholeCapacity;
        ByteBuffer bb = (ByteBuffer) bufferEntityPointer.getPages().get(page);
        byte isNull = bb.get(posEntity + colPosition);
        if (isNull == 0) {
            return false;
        }
        int entityStringSize = bb.get(posEntity + colPosition + 1);
        if (strValueToCompareSize != entityStringSize) {
            return false;
        }

        byte[] read = new byte[entityStringSize];
        bb.position(posEntity + colPosition + 5);
        bb.get(read);
        String entityStr = new String(read, StandardCharsets.UTF_8);
        return entityStr.equals(strValueToCompare);
    }
}
