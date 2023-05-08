package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.target.buffer.nonheap.access.field.BitFieldUnsignedIntegerNotNull;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class TestBitFieldUnsignedIntegerShort {

    @Test
    void test() {

        BitFieldUnsignedIntegerNotNull field = new BitFieldUnsignedIntegerNotNull();
        ByteBuffer bb = ByteBuffer.allocateDirect(20);

        field.writeValue(bb, 0, Integer.valueOf(Short.MAX_VALUE));
        Integer val = field.readValue(bb, 0);

        System.out.println(val);
        System.out.println(val == Short.MAX_VALUE);


    }
}
