package io.daobab.error;

import io.daobab.internallogger.ILoggerBean;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DaobabException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;
    private String statusDesc;


    public DaobabException(String msg) {
        super(msg);
        setStatusDesc(msg);
    }

    public DaobabException(ILoggerBean loggerbean, String msg) {
        this(msg);
        loggerbean.getLog().error(msg);
    }

    public DaobabException(String msg, Throwable th) {
        this(msg);
        initCause(th);
    }


    public DaobabException(ILoggerBean loggerbean, String msg, Throwable th) {
        this(msg);
        initCause(th);
        loggerbean.getLog().error(msg, th);
    }

    public String getStatusDesc() {
        return statusDesc;
    }


    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }


}
