package io.daobab.error;

import io.daobab.internallogger.ILoggerBean;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DaobabRollbackException extends RuntimeException {


    private static final long serialVersionUID = -1127875855361548L;


    public DaobabRollbackException(String msg) {
        super(msg);
    }

    public DaobabRollbackException(ILoggerBean loggerbean, String msg) {
        this(msg);
        loggerbean.getLog().error(msg);
    }


    public DaobabRollbackException(ILoggerBean loggerbean, String msg, Throwable th) {
        this(msg);
        initCause(th);
        loggerbean.getLog().error(msg, th);
    }


}
