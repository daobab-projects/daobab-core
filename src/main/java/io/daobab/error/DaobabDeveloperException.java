package io.daobab.error;

import io.daobab.internallogger.ILoggerBean;

public class DaobabDeveloperException extends DaobabException {
    public DaobabDeveloperException(String msg) {
        super(msg);
    }

    public DaobabDeveloperException(ILoggerBean loggerbean, String msg, Object... arguments) {
        super(loggerbean, msg, arguments);
    }

    public DaobabDeveloperException(ILoggerBean loggerbean, String msg) {
        super(loggerbean, msg);
    }

    public DaobabDeveloperException(String msg, Object... arguments) {
        super(msg, arguments);
    }

    public DaobabDeveloperException(String msg, Throwable th) {
        super(msg, th);
    }

    public DaobabDeveloperException(ILoggerBean loggerbean, String msg, Throwable th) {
        super(loggerbean, msg, th);
    }
}
