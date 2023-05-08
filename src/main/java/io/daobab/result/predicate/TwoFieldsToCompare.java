package io.daobab.result.predicate;

import io.daobab.target.buffer.nonheap.access.field.BitField;

import java.nio.ByteBuffer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TwoFieldsToCompare<F> {

    private Integer fieldOnePosition;
    private Integer fieldTwoPosition;
    private BitField<F> bitField;
    private ByteBuffer fieldOneByteBuffer;
    private ByteBuffer fieldTwoByteBuffer;

    public TwoFieldsToCompare(Integer fieldOnePosition, Integer fieldTwoPosition, BitField<F> bitField, ByteBuffer fieldOneByteBuffer, ByteBuffer fieldTwoByteBuffer) {
        this.fieldOnePosition = fieldOnePosition;
        this.fieldTwoPosition = fieldTwoPosition;
        this.bitField = bitField;
        this.fieldOneByteBuffer = fieldOneByteBuffer;
        this.fieldTwoByteBuffer = fieldTwoByteBuffer;
    }

    public Integer getFieldOnePosition() {
        return fieldOnePosition;
    }

    public void setFieldOnePosition(Integer fieldOnePosition) {
        this.fieldOnePosition = fieldOnePosition;
    }

    public Integer getFieldTwoPosition() {
        return fieldTwoPosition;
    }

    public void setFieldTwoPosition(Integer fieldTwoPosition) {
        this.fieldTwoPosition = fieldTwoPosition;
    }

    public BitField<F> getBitField() {
        return bitField;
    }

    public void setBitField(BitField<F> bitField) {
        this.bitField = bitField;
    }

    public ByteBuffer getFieldOneByteBuffer() {
        return fieldOneByteBuffer;
    }

    public void setFieldOneByteBuffer(ByteBuffer fieldOneByteBuffer) {
        this.fieldOneByteBuffer = fieldOneByteBuffer;
    }

    public ByteBuffer getFieldTwoByteBuffer() {
        return fieldTwoByteBuffer;
    }

    public void setFieldTwoByteBuffer(ByteBuffer fieldTwoByteBuffer) {
        this.fieldTwoByteBuffer = fieldTwoByteBuffer;
    }
}
