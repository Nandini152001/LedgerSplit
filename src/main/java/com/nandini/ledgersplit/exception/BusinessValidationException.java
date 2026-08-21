package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class BusinessValidationException extends SplitwiseException{

    public BusinessValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessValidationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
