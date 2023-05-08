package io.daobab.error;

import io.daobab.internallogger.ILoggerBean;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DaobabRollbackException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;

    public DaobabRollbackException(String msg) {
        super(msg);
    }

    public DaobabRollbackException(ILoggerBean loggerBean, String msg) {
        this(msg);
        loggerBean.getLog().error(msg);
    }

    public DaobabRollbackException(ILoggerBean loggerBean, String msg, Throwable th) {
        this(msg);
        initCause(th);
        loggerBean.getLog().error(msg, th);
    }


}
