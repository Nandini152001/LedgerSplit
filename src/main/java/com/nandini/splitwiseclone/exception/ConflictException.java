package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class ConflictException extends SplitwiseException{
    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ConflictException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
