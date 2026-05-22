package com.faust.ticketing.core.exception;

public class SystemException extends ApplicationException {
    private static final long serialVersionUID = -6786624554642943609L;

    public SystemException() {
    }

    public SystemException(final String message) {
        super(message);
    }

    public SystemException(final Exception ex) {
        super(ex);
    }
}
