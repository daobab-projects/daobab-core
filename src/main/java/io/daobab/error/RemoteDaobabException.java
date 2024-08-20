package io.daobab.error;

import io.daobab.model.ResponseWrapper;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class RemoteDaobabException extends DaobabException {

    public RemoteDaobabException(ResponseWrapper remote) {
        super((String) remote.getContent());
    }

}
