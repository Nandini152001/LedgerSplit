package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class ForbiddenOperationException extends SplitwiseException{

    public ForbiddenOperationException(ErrorCode errorCode, String message){
        super(errorCode, message);
    }

    public ForbiddenOperationException(ErrorCode errorCode, String message, Throwable cause){
        super(errorCode, message, cause);
    }

}
