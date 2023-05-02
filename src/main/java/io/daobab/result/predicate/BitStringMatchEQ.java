package io.daobab.result.predicate;

import io.daobab.target.buffer.nonheap.NonHeapBuffer;
import io.daobab.target.buffer.nonheap.NonHeapEntities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitStringMatchEQ extends MatchEQ {
    private final NonHeapEntities bufferEntityPointer;

    private final String strValueToCompare;
    private final int strValueToCompareSize;

    public BitStringMatchEQ(Object valueToCompare, NonHeapEntities bufferEntityPointer) {
        super(valueToCompare);
        this.strValueToCompare = (String) valueToCompare;
        this.strValueToCompareSize = strValueToCompare.length();
        this.bufferEntityPointer = bufferEntityPointer;
    }

    @Override
    public boolean bitTest(NonHeapBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
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
