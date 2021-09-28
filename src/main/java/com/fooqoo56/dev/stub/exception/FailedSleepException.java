package com.fooqoo56.dev.stub.exception;

public class FailedSleepException extends StubException {

    private static final long serialVersionUID = 3104744994690255379L;

    public FailedSleepException(final String message, final Throwable exception) {
        super(message, exception);
    }
}
