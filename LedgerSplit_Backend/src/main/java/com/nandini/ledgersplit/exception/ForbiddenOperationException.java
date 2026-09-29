package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class ForbiddenOperationException extends SplitwiseException{

    public ForbiddenOperationException(ErrorCode errorCode, String message){
        super(errorCode, message);
    }

    public ForbiddenOperationException(ErrorCode errorCode, String message, Throwable cause){
        super(errorCode, message, cause);
    }

}
