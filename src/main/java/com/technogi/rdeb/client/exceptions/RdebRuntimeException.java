package com.technogi.rdeb.client.exceptions;

public class RdebRuntimeException extends RuntimeException {
    public RdebRuntimeException() {
    }

    public RdebRuntimeException(String message) {
        super(message);
    }

    public RdebRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RdebRuntimeException(Throwable cause) {
        super(cause);
    }

    public RdebRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
