package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class ConflictException extends SplitwiseException{
    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ConflictException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
