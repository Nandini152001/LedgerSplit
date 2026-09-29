package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

//One Common Base Exception instead of extending RuntimeException
public class SplitwiseException extends RuntimeException{

    private final ErrorCode errorCode;

    public ErrorCode getErrorCode(){
        return errorCode;
    }

    public SplitwiseException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public SplitwiseException(ErrorCode errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
    }

}
