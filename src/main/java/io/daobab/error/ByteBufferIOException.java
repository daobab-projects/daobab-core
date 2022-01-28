package io.daobab.error;

import io.daobab.model.TableColumn;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class ByteBufferIOException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public ByteBufferIOException(TableColumn ic) {
        super(String.format("ByteBuffer problem ith field %s", ic.getColumn().toString()));
    }

    public ByteBufferIOException(TableColumn ic, Exception root) {
        super(String.format("ByteBuffer problem ith field %s", ic.getColumn().toString()), root);
    }

}
