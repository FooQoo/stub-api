package com.fooqoo56.dev.stub.exception;

public abstract class StubException extends RuntimeException {

    private static final long serialVersionUID = 4256829027213368417L;

    public StubException(final String message) {
        super(message);
    }

    public StubException(final String message, final Throwable exception) {
        super(message, exception);
    }
}
