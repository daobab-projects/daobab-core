package io.daobab.error;

import io.daobab.internallogger.ILoggerBean;

import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class DaobabException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;
    private String statusDesc;

    public DaobabException(String msg) {
        super(msg);
        setStatusDesc(msg);
    }

    public DaobabException(ILoggerBean loggerbean, String msg, Object... arguments) {
        this(format(msg, arguments));
        loggerbean.getLog().error(format(msg, arguments));
    }

    public DaobabException(ILoggerBean loggerbean, String msg) {
        this(msg);
        loggerbean.getLog().error(msg);
    }

    public DaobabException(String msg, Object... arguments) {
        this(format(msg, arguments));
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
