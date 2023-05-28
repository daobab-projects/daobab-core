package io.daobab.error;

import io.daobab.model.TableColumn;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ByteBufferIOException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public ByteBufferIOException(TableColumn ic) {
        super(String.format("ByteBuffer problem at field %s", ic.getColumn().toString()));
    }

    public ByteBufferIOException(TableColumn ic, Exception root) {
        super(String.format("ByteBuffer problem at field %s", ic.getColumn().toString()), root);
    }

}
