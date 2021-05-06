package io.daobab.result;

public class WrapOffsetAndSize {
    private final int offset;
    private final int size;

    public WrapOffsetAndSize(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }
}
