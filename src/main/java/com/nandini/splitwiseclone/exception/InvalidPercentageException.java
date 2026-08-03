package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class InvalidPercentageException extends BusinessValidationException{

    public InvalidPercentageException() {
        super(ErrorCode.INVALID_PERCENTAGE, ("Total percentage of splits must be 100%."));
    }
}
