package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class ResourceNotFoundException extends  SplitwiseException{

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);

    }

    public ResourceNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
