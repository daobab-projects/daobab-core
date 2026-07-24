package io.daobab.model;

/**
 * A response envelope carrying the {@link #getContent() content}, a {@link #getStatus() status} and an optional
 * {@link #getException() exception} - used to return a result (or an error) across a remote boundary.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ResponseWrapper {

    private Object content;

    private String status;

    private Exception exception;

    /**
     * The response status.
     */
    public String getStatus() {
        return status;
    }

    /** Sets the response status. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** The response payload. */
    public Object getContent() {
        return content;
    }

    /** Sets the response payload. */
    public void setContent(Object content) {
        this.content = content;
    }

    /** The error, when the response carries one. */
    public Exception getException() {
        return exception;
    }

    /** Sets the error. */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
