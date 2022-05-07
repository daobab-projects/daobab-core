package io.daobab.error;

import io.daobab.model.ResponseWrapper;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class RemoteDaobabException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public RemoteDaobabException(ResponseWrapper remote) {
        super((String) remote.getContent());
    }

}
