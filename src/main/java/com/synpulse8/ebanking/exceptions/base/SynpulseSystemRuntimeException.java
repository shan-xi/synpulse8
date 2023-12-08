package com.synpulse8.ebanking.exceptions.base;

/**
 * SynpulseSystemException is an abstract highest Exception in Synpulse System,<br>
 * this exception will be handled in global level
 */
public abstract class SynpulseSystemRuntimeException extends RuntimeException {

    protected SynpulseSystemRuntimeException() {
    }

    protected SynpulseSystemRuntimeException(String message) {
        super(message);
    }

    protected SynpulseSystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    protected SynpulseSystemRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " : " + getMessage();
    }

    public abstract String getCode();

    @Override
    public abstract String getMessage();

}
