package com.synpulse8.ebanking.exceptions.base;

/**
 * SynpulseSystemException is an abstract highest Exception in Synpulse System,<br>
 * this exception will be handled in global level
 */
public abstract class SynpulseSystemException extends Exception {

    public SynpulseSystemException() {
    }

    public SynpulseSystemException(String message) {
        super(message);
    }

    public SynpulseSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " : " + getMessage();
    }

    public abstract String getCode();

    public abstract String getMessage();

}
