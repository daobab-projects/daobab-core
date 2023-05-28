package io.daobab.target.buffer.single.function;

public class FunctionTypeConverter {

    public Long toLong(String value) {
        return value == null ? null : new Long(value);
    }

    public Integer toInteger(String value) {
        return value == null ? null : new Integer(value);
    }
}
