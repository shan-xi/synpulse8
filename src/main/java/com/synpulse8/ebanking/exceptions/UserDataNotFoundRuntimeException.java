package com.synpulse8.ebanking.exceptions;

import com.synpulse8.ebanking.exceptions.base.SynpulseSystemRuntimeException;

public class UserDataNotFoundRuntimeException extends SynpulseSystemRuntimeException {

    private static final String CODE = "USER_DATA_NOT_FOUND";

    private String message = "USER_DATA_NOT_FOUND";

    public UserDataNotFoundRuntimeException() {
    }

    public UserDataNotFoundRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
